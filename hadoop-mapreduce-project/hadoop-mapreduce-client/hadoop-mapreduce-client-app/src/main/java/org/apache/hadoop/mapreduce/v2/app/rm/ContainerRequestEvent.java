/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.apache.hadoop.mapreduce.v2.app.rm;

import org.apache.hadoop.mapreduce.v2.api.records.TaskAttemptId;
import org.apache.hadoop.yarn.api.records.Resource;


public class ContainerRequestEvent extends ContainerAllocatorEvent {
  
  private final Resource capability;
  private final String[] hosts;
  private final String[] racks;
  private boolean earlierAttemptFailed = false;
  private boolean resumeAttempt = false;

  public ContainerRequestEvent(TaskAttemptId attemptID, 
      Resource capability,
      String[] hosts, String[] racks) {
    super(attemptID, ContainerAllocator.EventType.CONTAINER_REQ);
    this.capability = capability;
    this.hosts = hosts;
    this.racks = racks;
  }
  
  public ContainerRequestEvent(TaskAttemptId attemptID, 
      Resource capability,
      String[] hosts, String[] racks,
      boolean resumeAttempt) {
    this(attemptID, capability, hosts, racks);
    this.resumeAttempt = true;
  }
  
  ContainerRequestEvent(TaskAttemptId attemptID, Resource capability) {
    this(attemptID, capability, new String[0], new String[0]);
    this.earlierAttemptFailed = true;
  }
  
  public static ContainerRequestEvent createContainerRequestEventForFailedContainer(
      TaskAttemptId attemptID, 
      Resource capability) {
    //ContainerRequest for failed events does not consider rack / node locality?
    return new ContainerRequestEvent(attemptID, capability);
  }

  public Resource getCapability() {
    return capability;
  }

  public String[] getHosts() {
    return hosts;
  }
  
  public String[] getRacks() {
    return racks;
  }
  
  public boolean getEarlierAttemptFailed() {
    return earlierAttemptFailed;
  }

  public boolean isResumeAttempt() {
    return resumeAttempt;
  }
}