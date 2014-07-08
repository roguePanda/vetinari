/**
 * Copyright 2014 Ben Navetta
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
package com.bennavetta.vetinari.build;

import com.bennavetta.vetinari.build.phase.DefaultOutputPhase;
import com.bennavetta.vetinari.build.phase.LayoutPhase;
import com.bennavetta.vetinari.build.phase.RenderPhase;
import com.bennavetta.vetinari.build.phase.TemplatePhase;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Guice module for the build process.
 */
public class BuildModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(SiteBuilder.class);

		Multibinder<BuildPhase> buildPhaseMultibinder = Multibinder.newSetBinder(binder(), BuildPhase.class);
		buildPhaseMultibinder.addBinding().to(TemplatePhase.class);
		buildPhaseMultibinder.addBinding().to(RenderPhase.class);
		buildPhaseMultibinder.addBinding().to(LayoutPhase.class);
		buildPhaseMultibinder.addBinding().to(DefaultOutputPhase.class);
	}
}
