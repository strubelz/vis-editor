/*
 * Copyright 2014-2016 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kotcrab.vis.ui.util.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Value that returns widget preferred height if it's visible. If widget is invisible then returns 0.
 * This can be only added to classes extending {@link Widget} or {@link Table}, if you try to add it to any other class
 * you will get {@link IllegalStateException} during runtime.
 * @author Kotcrab
 * @since 0.9.3
 */
public class PrefHeightIfVisibleValue extends Value {
	public static final PrefHeightIfVisibleValue INSTANCE = new PrefHeightIfVisibleValue();

	@Override
	public float get (Actor actor) {
		if (actor instanceof Widget) {
			Widget widget = (Widget) actor;
			return widget.isVisible() ? widget.getPrefHeight() : 0;
		}

		if (actor instanceof Table) {
			Table table = (Table) actor;
			return table.isVisible() ? table.getPrefHeight() : 0;
		}

		throw new IllegalStateException("Unsupported actor type for PrefHeightIfVisibleValue: " + actor.getClass());
	}
}
