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
package org.santfeliu.security.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Set;
import org.gdmatrix.setup.AbstractSetup;
import org.gdmatrix.setup.GlobalSetup;
import org.santfeliu.security.encoder.DigestEncoder;

/**
 *
 * @author realor
 */
@ApplicationScoped
public class Setup extends AbstractSetup
{
  @Inject
  GlobalSetup globalSetup;

  String masterPassword = "changeme";
  Set<String> certUserRoles;
  boolean validateCertificate = false;
  String digestEncoderClass = "org.santfeliu.security.encoder.MatrixDigestEncoder";
  String digestParameters;
  int minUserLength = 4;
  int minPasswordLength = 4;
  String defaultPersonId = "0";
  String userLockControlMode = "off";
  int maxFailedLoginAttempts = 5;
  int autoUnlockMarginTime = 600;
  int minIntrusionAttempts = 10;
  String authUserPasswordCipherSecret = "changeme";
  boolean ldapEnabled = false;
  String ldapAdminUserId = "admin";
  String ldapAdminPassword = "changeme";
  String ldapUrl = "localhost";
  String ldapDomain = null;
  String ldapBase = null;


  public String getMasterPassword()
  {
    return masterPassword;
  }

  public void setMasterPassword(String masterPassword)
  {
    this.masterPassword = masterPassword;
  }

  public Set<String> getCertUserRoles()
  {
    return certUserRoles;
  }

  public void setCertUserRoles(Set<String> certUserRoles)
  {
    this.certUserRoles = certUserRoles;
  }

  public boolean isValidateCertificate()
  {
    return validateCertificate;
  }

  public void setValidateCertificate(boolean validateCertificate)
  {
    this.validateCertificate = validateCertificate;
  }

  public String getDigestEncoderClass()
  {
    return digestEncoderClass;
  }

  public void setDigestEncoderClass(String digestEncoderClass)
  {
    this.digestEncoderClass = digestEncoderClass;
  }

  public String getDigestParameters()
  {
    return digestParameters;
  }

  public void setDigestParameters(String digestParameters)
  {
    this.digestParameters = digestParameters;
  }

  public int getMinUserLength()
  {
    return minUserLength;
  }

  public void setMinUserLength(int minUserLength)
  {
    this.minUserLength = minUserLength;
  }

  public int getMinPasswordLength()
  {
    return minPasswordLength;
  }

  public void setMinPasswordLength(int minPasswordLength)
  {
    this.minPasswordLength = minPasswordLength;
  }

  public String getDefaultPersonId()
  {
    return defaultPersonId;
  }

  public void setDefaultPersonId(String defaultPersonId)
  {
    this.defaultPersonId = defaultPersonId;
  }

  public String getUserLockControlMode()
  {
    return userLockControlMode;
  }

  public void setUserLockControlMode(String userLockControlMode)
  {
    this.userLockControlMode = userLockControlMode;
  }

  public int getMaxFailedLoginAttempts()
  {
    return maxFailedLoginAttempts;
  }

  public void setMaxFailedLoginAttempts(int maxFailedLoginAttempts)
  {
    this.maxFailedLoginAttempts = maxFailedLoginAttempts;
  }

  public int getAutoUnlockMarginTime()
  {
    return autoUnlockMarginTime;
  }

  public void setAutoUnlockMarginTime(int autoUnlockMarginTime)
  {
    this.autoUnlockMarginTime = autoUnlockMarginTime;
  }

  public int getMinIntrusionAttempts()
  {
    return minIntrusionAttempts;
  }

  public void setMinIntrusionAttempts(int minIntrusionAttempts)
  {
    this.minIntrusionAttempts = minIntrusionAttempts;
  }

  public GlobalSetup getGlobalSetup()
  {
    return globalSetup;
  }

  public String getAuthUserPasswordCipherSecret()
  {
    return authUserPasswordCipherSecret;
  }

  public void setAuthUserPasswordCipherSecret(String authUserPasswordCipherSecret)
  {
    this.authUserPasswordCipherSecret = authUserPasswordCipherSecret;
  }

  public boolean isLdapEnabled()
  {
    return ldapEnabled;
  }

  public void setLdapEnabled(boolean ldapEnabled)
  {
    this.ldapEnabled = ldapEnabled;
  }

  public String getLdapAdminUserId()
  {
    return ldapAdminUserId;
  }

  public void setLdapAdminUserId(String ldapAdminUserId)
  {
    this.ldapAdminUserId = ldapAdminUserId;
  }

  public String getLdapAdminPassword()
  {
    return ldapAdminPassword;
  }

  public void setLdapAdminPassword(String ldapAdminPassword)
  {
    this.ldapAdminPassword = ldapAdminPassword;
  }

  public String getLdapUrl()
  {
    return ldapUrl;
  }

  public void setLdapUrl(String ldapUrl)
  {
    this.ldapUrl = ldapUrl;
  }

  public String getLdapDomain()
  {
    return ldapDomain;
  }

  public void setLdapDomain(String ldapDomain)
  {
    this.ldapDomain = ldapDomain;
  }

  public String getLdapBase()
  {
    return ldapBase;
  }

  public void setLdapBase(String ldapBase)
  {
    this.ldapBase = ldapBase;
  }

  public DigestEncoder getDigestEncoder() throws Exception
  {
    Class<?> cls = Class.forName(digestEncoderClass);
    return (DigestEncoder)cls.getConstructor().newInstance();
  }
}
