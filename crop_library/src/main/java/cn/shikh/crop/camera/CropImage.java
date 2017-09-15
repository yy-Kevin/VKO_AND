/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.shikh.crop.camera;


import android.widget.TextView;

import cn.shikh.crop.R;

/**
 * The activity can crop specific region of interest from an image.
 */
public class CropImage extends BaseCropImage {
	@Override
	public int getContextView() {
		// TODO Auto-generated method stub
		return R.layout.cropimage;
	}

	@Override
	public CropImageView getCropImageView() {
		return (CropImageView) findViewById(R.id.image);
	}

	@Override
	public TextView getDisCardView() {
		return (TextView) findViewById(R.id.discard);
	}

	@Override
	public TextView getSaveView() {
		return (TextView) findViewById(R.id.save);
	}

}
