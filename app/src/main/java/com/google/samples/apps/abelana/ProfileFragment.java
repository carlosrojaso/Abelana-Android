/*
 * Copyright 2014 Google Inc. All rights reserved.
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

package com.google.samples.apps.abelana;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Fragment used to display the user's profile
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        //Refer to FeedFragment for a full explanation on how these API calls work
        AbelanaClient client = new AbelanaClient();
        final GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        client.mGetMyProfile.getMyProfile(Data.aTok, "0", new Callback<AbelanaClient.Timeline>() {
            @Override
            public void success(AbelanaClient.Timeline timeline, Response response) {
                Data.mProfileUrls = new ArrayList<String>();
                if (timeline.entries != null) {
                    for (AbelanaClient.TimelineEntry e : timeline.entries) {
                        Data.mProfileUrls.add(AbelanaThings.getImage(e.photoid));
                    }
                    gridView.setAdapter(new ProfileAdapter(getActivity()));
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

        final TextView followersText = (TextView) rootView.findViewById(R.id.profile_text_2);
        final TextView followingText = (TextView) rootView.findViewById(R.id.profile_text_1);

        client.mStatistics.statistics(Data.aTok, new Callback<AbelanaClient.Stats>() {
            @Override
            public void success(AbelanaClient.Stats stats, Response response) {
                followersText.setText(stats.followers + " followers");
                followingText.setText(stats.following + " following");
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        return rootView;

    }
}
