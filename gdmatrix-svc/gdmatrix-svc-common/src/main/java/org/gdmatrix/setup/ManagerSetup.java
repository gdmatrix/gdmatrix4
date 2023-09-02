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
package org.gdmatrix.setup;

import jakarta.enterprise.context.ApplicationScoped;

/**
 *
 * @author realor
 */
@ApplicationScoped
public class ManagerSetup extends AbstractSetup
{
  String securityManagerImpl = "org.santfeliu.security.service.SecurityManager";
  String kernelManagerImpl = "com.audifilm.gdmatrix.svc.kernel.KernelManager";
  String caseManagerImpl;
  String documentManagerImpl = "org.santfeliu.gdmatrix.svc.doc.DocumentManager";
  String dictionaryManagerImpl;
  String agendaManagerImpl;
  String CMSManagerImpl;
  String classificationManagerImpl;
  String newsManagerImpl;
  String forumManagerImpl;
  String signatureManagerImpl;
  String feedManagerImpl;
  String electionsManagerImpl;
  String jobManagerImpl;
  String policyManagerImpl;
  String presenceManagerImpl;
  String reportManagerImpl;
  String SQLManagerImpl;
  String surveyManagerImpl;
  String translationManagerImpl;
  String workflowManagerImpl;

  public String getSecurityManagerImpl()
  {
    return securityManagerImpl;
  }

  public void setSecurityManagerImpl(String securityManagerImpl)
  {
    this.securityManagerImpl = securityManagerImpl;
  }

  public String getKernelManagerImpl()
  {
    return kernelManagerImpl;
  }

  public void setKernelManagerImpl(String kernelManagerImpl)
  {
    this.kernelManagerImpl = kernelManagerImpl;
  }

  public String getCaseManagerImpl()
  {
    return caseManagerImpl;
  }

  public void setCaseManagerImpl(String caseManagerImpl)
  {
    this.caseManagerImpl = caseManagerImpl;
  }

  public String getDocumentManagerImpl()
  {
    return documentManagerImpl;
  }

  public void setDocumentManagerImpl(String documentManagerImpl)
  {
    this.documentManagerImpl = documentManagerImpl;
  }

  public String getDictionaryManagerImpl()
  {
    return dictionaryManagerImpl;
  }

  public void setDictionaryManagerImpl(String dictionaryManagerImpl)
  {
    this.dictionaryManagerImpl = dictionaryManagerImpl;
  }

  public String getAgendaManagerImpl()
  {
    return agendaManagerImpl;
  }

  public void setAgendaManagerImpl(String agendaManagerImpl)
  {
    this.agendaManagerImpl = agendaManagerImpl;
  }

  public String getCMSManagerImpl()
  {
    return CMSManagerImpl;
  }

  public void setCMSManagerImpl(String CMSManagerImpl)
  {
    this.CMSManagerImpl = CMSManagerImpl;
  }

  public String getClassificationManagerImpl()
  {
    return classificationManagerImpl;
  }

  public void setClassificationManagerImpl(String classificationManagerImpl)
  {
    this.classificationManagerImpl = classificationManagerImpl;
  }

  public String getNewsManagerImpl()
  {
    return newsManagerImpl;
  }

  public void setNewsManagerImpl(String newsManagerImpl)
  {
    this.newsManagerImpl = newsManagerImpl;
  }

  public String getForumManagerImpl()
  {
    return forumManagerImpl;
  }

  public void setForumManagerImpl(String forumManagerImpl)
  {
    this.forumManagerImpl = forumManagerImpl;
  }

  public String getSignatureManagerImpl()
  {
    return signatureManagerImpl;
  }

  public void setSignatureManagerImpl(String signatureManagerImpl)
  {
    this.signatureManagerImpl = signatureManagerImpl;
  }

  public String getFeedManagerImpl()
  {
    return feedManagerImpl;
  }

  public void setFeedManagerImpl(String feedManagerImpl)
  {
    this.feedManagerImpl = feedManagerImpl;
  }

  public String getElectionsManagerImpl()
  {
    return electionsManagerImpl;
  }

  public void setElectionsManagerImpl(String electionsManagerImpl)
  {
    this.electionsManagerImpl = electionsManagerImpl;
  }

  public String getJobManagerImpl()
  {
    return jobManagerImpl;
  }

  public void setJobManagerImpl(String jobManagerImpl)
  {
    this.jobManagerImpl = jobManagerImpl;
  }

  public String getPolicyManagerImpl()
  {
    return policyManagerImpl;
  }

  public void setPolicyManagerImpl(String policyManagerImpl)
  {
    this.policyManagerImpl = policyManagerImpl;
  }

  public String getPresenceManagerImpl()
  {
    return presenceManagerImpl;
  }

  public void setPresenceManagerImpl(String presenceManagerImpl)
  {
    this.presenceManagerImpl = presenceManagerImpl;
  }

  public String getReportManagerImpl()
  {
    return reportManagerImpl;
  }

  public void setReportManagerImpl(String reportManagerImpl)
  {
    this.reportManagerImpl = reportManagerImpl;
  }

  public String getSQLManagerImpl()
  {
    return SQLManagerImpl;
  }

  public void setSQLManagerImpl(String SQLManagerImpl)
  {
    this.SQLManagerImpl = SQLManagerImpl;
  }

  public String getSurveyManagerImpl()
  {
    return surveyManagerImpl;
  }

  public void setSurveyManagerImpl(String surveyManagerImpl)
  {
    this.surveyManagerImpl = surveyManagerImpl;
  }

  public String getTranslationManagerImpl()
  {
    return translationManagerImpl;
  }

  public void setTranslationManagerImpl(String translationManagerImpl)
  {
    this.translationManagerImpl = translationManagerImpl;
  }

  public String getWorkflowManagerImpl()
  {
    return workflowManagerImpl;
  }

  public void setWorkflowManagerImpl(String workflowManagerImpl)
  {
    this.workflowManagerImpl = workflowManagerImpl;
  }
}
