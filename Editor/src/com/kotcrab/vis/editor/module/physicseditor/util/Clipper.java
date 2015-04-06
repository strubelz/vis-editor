/*
 * Copyright 2014-2015 Pawel Pastuszak
 *
 * This file is part of VisEditor.
 *
 * VisEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VisEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VisEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.kotcrab.vis.editor.module.physicseditor.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.editor.module.physicseditor.util.earclipping.bayazit.BayazitDecomposer;
import com.kotcrab.vis.editor.module.physicseditor.util.earclipping.ewjordan.EwjordanDecomposer;
import com.kotcrab.vis.runtime.util.PrettyEnum;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class Clipper {
	public enum Polygonizer implements PrettyEnum {
		EWJORDAN {
			@Override
			public String toPrettyString () {
				return "Ewjordan";
			}
		}, BAYAZIT {
			@Override
			public String toPrettyString () {
				return "Bayazit";
			}
		}
	}

	public static Vector2[][] polygonize (Polygonizer polygonizer, Vector2[] points) {
		Vector2[][] polygons = null;

		if (PolygonUtils.isPolygonCCW(points))
			ArrayUtils.reverse(points);

		switch (polygonizer) {
			case EWJORDAN:
				polygons = EwjordanDecomposer.decompose(points);
				break;

			case BAYAZIT:
				Array<Vector2> tmpPoints = new Array<Vector2>(points.length);
				tmpPoints.addAll(points);

				Array<Array<Vector2>> tmpPolygons;

				try {
					tmpPolygons = BayazitDecomposer.ConvexPartition(tmpPoints);
				} catch (Exception ex) {
					tmpPolygons = null;
				}

				if (tmpPolygons != null) {
					polygons = new Vector2[tmpPolygons.size][];
					for (int i = 0; i < tmpPolygons.size; i++) {
						polygons[i] = new Vector2[tmpPolygons.get(i).size];
						for (int ii = 0; ii < tmpPolygons.get(i).size; ii++)
							polygons[i][ii] = new Vector2(tmpPolygons.get(i).get(ii));
					}
				}
				break;
		}

		if (polygons != null) polygons = sliceForMax8Vertices(polygons);
		return polygons;
	}

	private static Vector2[][] sliceForMax8Vertices (Vector2[][] polygons) {
		for (int i = 0; i < polygons.length; i++) {
			Vector2[] poly = polygons[i];
			if (poly.length > 8) {
				int limit = poly.length < 15 ? poly.length / 2 + 1 : 8;
				Vector2[] newPoly1 = new Vector2[limit];
				Vector2[] newPoly2 = new Vector2[poly.length - limit + 2];
				System.arraycopy(poly, 0, newPoly1, 0, limit);
				System.arraycopy(poly, limit - 1, newPoly2, 0, poly.length - limit + 1);
				newPoly2[newPoly2.length - 1] = poly[0].cpy();

				Vector2[][] newPolys = new Vector2[polygons.length + 1][];
				if (i > 0) {
					System.arraycopy(polygons, 0, newPolys, 0, i);
				}
				if (i < polygons.length - 1) {
					System.arraycopy(polygons, i + 1, newPolys, i + 2, polygons.length - i - 1);
				}
				newPolys[i] = newPoly1;
				newPolys[i + 1] = newPoly2;
				polygons = newPolys;

				i -= 1;
			}
		}
		return polygons;
	}
}
