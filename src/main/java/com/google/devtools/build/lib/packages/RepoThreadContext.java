// Copyright 2023 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.packages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.devtools.build.lib.cmdline.StarlarkThreadContext;
import java.util.Collection;
import java.util.Map;
import net.starlark.java.eval.EvalException;
import net.starlark.java.eval.Starlark;
import net.starlark.java.eval.StarlarkThread;

/** Context object for a Starlark thread evaluating the REPO.bazel file. */
public class RepoThreadContext extends StarlarkThreadContext {
  private ImmutableMap<String, Object> packageArgsMap = ImmutableMap.of();
  private boolean repoFunctionCalled = false;

  private ImmutableList<String> ignoredDirectories = ImmutableList.of();
  private boolean ignoredDirectoriesSet = false;

  public static RepoThreadContext fromOrFail(StarlarkThread thread, String what)
      throws EvalException {
    StarlarkThreadContext context = thread.getThreadLocal(StarlarkThreadContext.class);
    if (context instanceof RepoThreadContext c) {
      return c;
    }
    throw Starlark.errorf("%s can only be called from REPO.bazel", what);
  }

  public RepoThreadContext() {
    super(() -> null);
  }

  public boolean isRepoFunctionCalled() {
    return repoFunctionCalled;
  }

  public void setPackageArgsMap(Map<String, Object> kwargs) {
    repoFunctionCalled = true;
    this.packageArgsMap = ImmutableMap.copyOf(kwargs);
  }

  public ImmutableMap<String, Object> getPackageArgsMap() {
    return packageArgsMap;
  }

  public void setIgnoredDirectories(Collection<String> ignoredDirectories) throws EvalException {
    ignoredDirectoriesSet = true;
    this.ignoredDirectories = ImmutableList.copyOf(ignoredDirectories);
  }

  public boolean isIgnoredDirectoriesSet() {
    return ignoredDirectoriesSet;
  }

  public ImmutableList<String> getIgnoredDirectories() {
    return ignoredDirectories;
  }
}
