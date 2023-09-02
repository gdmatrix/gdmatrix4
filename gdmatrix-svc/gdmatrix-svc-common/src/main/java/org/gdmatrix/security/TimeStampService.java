/*
 * GDMatrix
 *
 * Copyright (C) 2020-2023, Ajuntament de Sant Feliu de Llobregat
 *
 * This program is licensed and may be used, modified and redistributed under
 * the terms of the European Public License (EUPL), either version 1.1 or (at
 * your option) any later version as soon as they are approved by the European
 * Commission.
 *
 * Alternatively, you may redistribute and/or modify this program under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either  version 3 of the License, or (at your option)
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the licenses for the specific language governing permissions, limitations
 * and more details.
 *
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along
 * with this program; if not, you may find them at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * http://www.gnu.org/licenses/
 * and
 * https://www.gnu.org/licenses/lgpl.txt
 */
package org.gdmatrix.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.tsp.MessageImprint;
import org.bouncycastle.asn1.tsp.TimeStampReq;
import org.bouncycastle.asn1.tsp.TimeStampResp;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import org.gdmatrix.setup.AbstractSetup;

/**
 *
 * @author realor
 */
@ApplicationScoped
public class TimeStampService
{
  @Inject
  Setup setup;

  private static final Logger LOGGER = Logger.getLogger("TimeStampService");
  private final SecureRandom random = new SecureRandom();

  public Setup getSetup()
  {
    return setup;
  }

  public void setSetup(Setup setup)
  {
    this.setup = setup;
  }

  public TimeStampToken timestamp(byte[] hash, String hashAlgorithm)
    throws IOException, TSPException
  {
    return timestamp(hash, hashAlgorithm, null);
  }

  public TimeStampToken timestamp(byte[] hash, String hashAlgorithm,
    String policyOid) throws IOException, TSPException
  {
    String url = setup.getUrl();

    LOGGER.log(Level.INFO, "Generating timestamp with TSA {0}", url);
    AlgorithmIdentifier hashOid = getHashOid(hashAlgorithm);
    MessageImprint imprint = new MessageImprint(hashOid, hash);

    ASN1ObjectIdentifier tsaPolicyId = StringUtils.isBlank(policyOid) ?
      null : new ASN1ObjectIdentifier(policyOid);

    TimeStampReq request = new TimeStampReq(imprint, tsaPolicyId,
      new ASN1Integer(random.nextLong()), ASN1Boolean.TRUE, null);

    byte[] body = request.getEncoded();
    try
    {
      byte[] responseBytes = getTSAResponse(body);

      ASN1StreamParser asn1Sp = new ASN1StreamParser(responseBytes);
      TimeStampResp tspResp = TimeStampResp.getInstance(asn1Sp.readObject());
      TimeStampResponse tsr = new TimeStampResponse(tspResp);

      checkForErrors(url, tsr);

      // validate communication level attributes (RFC 3161 PKIStatus)
      tsr.validate(new TimeStampRequest(request));

      return tsr.getTimeStampToken();
    }
    catch (Exception e)
    {
      throw new IOException(e);
    }
  }

  private void checkForErrors(String tsaUrl, TimeStampResponse tsr)
    throws IOException
  {
    PKIFailureInfo failure = tsr.getFailInfo();
    int value = (failure == null) ? 0 : failure.intValue();
    if (value != 0)
    {
      throw new IOException("Invalid TSA '" + tsaUrl +
        "' response, code: " + Integer.toHexString(value));
    }
  }

  protected byte[] getTSAResponse(byte[] requestBytes) throws IOException
  {
    URL tspUrl = new URL(setup.getUrl());
    URLConnection tsaConnection = tspUrl.openConnection();
    tsaConnection.setConnectTimeout(10000);
    tsaConnection.setDoInput(true);
    tsaConnection.setDoOutput(true);
    tsaConnection.setUseCaches(false);
    tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
    tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");

    if (StringUtils.isNotBlank(setup.getUsername()))
    {
      String userPassword = setup.getUsername() + ":" +
        setup.getPassword();
      tsaConnection.setRequestProperty("Authorization", "Basic "
        + Base64.getEncoder().encodeToString(userPassword.getBytes()));
    }

    try (OutputStream out = tsaConnection.getOutputStream())
    {
      out.write(requestBytes);
    }

    byte[] respBytes;
    try (InputStream input = tsaConnection.getInputStream())
    {
      respBytes = IOUtils.toByteArray(input);
    }

    String encoding = tsaConnection.getContentEncoding();
    if (encoding != null && encoding.equalsIgnoreCase("base64"))
    {
      respBytes = Base64.getDecoder().decode(respBytes);
    }
    return respBytes;
  }

  private AlgorithmIdentifier getHashOid(String hashAlgorithm)
  {
    DigestAlgorithmIdentifierFinder algorithmFinder =
      new DefaultDigestAlgorithmIdentifierFinder();
    return algorithmFinder.find(hashAlgorithm);
  }

  @ApplicationScoped
  public static class Setup extends AbstractSetup
  {
    String url;
    String username;
    String password;

    public String getUrl()
    {
      return url;
    }

    public void setUrl(String url)
    {
      this.url = url;
    }

    public String getUsername()
    {
      return username;
    }

    public void setUsername(String username)
    {
      this.username = username;
    }

    public String getPassword()
    {
      return password;
    }

    public void setPassword(String password)
    {
      this.password = password;
    }
  }

  public static void main(String[] args)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.reset();
      md.update("HOLA".getBytes());
      byte[] hash = md.digest();

      Setup setup = new Setup();
      setup.setUrl("http://timestamp.sectigo.com/qualified");

      TimeStampService tss = new TimeStampService();
      tss.setSetup(setup);
      TimeStampToken timestamp = tss.timestamp(hash, "SHA-256", null);
      System.out.println(timestamp);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
