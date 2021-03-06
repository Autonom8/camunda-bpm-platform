/*
 * Copyright © 2013-2019 camunda services GmbH and various authors (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.cmd;

import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotEmpty;
import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotNull;

import java.io.Serializable;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.impl.cfg.CommandChecker;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;

/**
 * @author Tobias Metzke
 *
 */
public class DeleteHistoricVariableInstanceCmd implements Command<Void>, Serializable {

  private String variableInstanceId;

  public DeleteHistoricVariableInstanceCmd(String variableInstanceId) {
    this.variableInstanceId = variableInstanceId;
  }
  
  @Override
  public Void execute(CommandContext commandContext) {
    ensureNotEmpty(BadUserRequestException.class,"variableInstanceId", variableInstanceId);

    HistoricVariableInstanceEntity variable = commandContext.getHistoricVariableInstanceManager().findHistoricVariableInstanceByVariableInstanceId(variableInstanceId);
    ensureNotNull("No historic variable instance found with id: " + variableInstanceId, "variable", variable);
    
    for(CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
      checker.checkDeleteHistoricVariableInstance(variable);
    }

    commandContext
      .getHistoricDetailManager()
      .deleteHistoricDetailsByVariableInstanceId(variableInstanceId);
    
    commandContext
      .getHistoricVariableInstanceManager()
      .deleteHistoricVariableInstanceByVariableInstanceId(variableInstanceId);
    return null;
  }
}
