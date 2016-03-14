/*
 * Copyright (C) 2014 The Android Open Source Project
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
package co.fabrk.popmovies.discover;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.fabrk.popmovies.data.TmdbServiceApi;
import co.fabrk.popmovies.discover.DiscoverPresenter;
import co.fabrk.popmovies.discover.DiscoverContract;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.fail;


public class getPopularMovieListDiscoverPresenterTest {

    @Mock
    private TmdbServiceApi mTmdbServiceApi;

    @Mock
    private DiscoverContract.View mDiscoverView;

    private DiscoverPresenter mDiscoverPresenter;

    @Before
    public void setupDiscoverPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mDiscoverPresenter = new DiscoverPresenter(mTmdbServiceApi, mDiscoverView);
    }

    @Test
    public void getPopularMovieList_showGrid() {
        // When the presenter is asked to return a cursor to the list of popular movies
//        fail("Implement in step 6");

//        mDiscoverPresenter.getPopularMovieList();
//
//        // Then a request is sent to the service provider
//        verify(mTmdbServiceApi).getUri();
//        // Then the gridview is updated
        verify(mDiscoverView).showMovieGrid();
    }

//    @Test
//    public void getPopularMovieList_emptyCursor() {
//        // When the presenter is asked to save a note
//        mDiscoverPresenter.getPopularMovieList();
//
//        // Then a note is,
//        verify(mNotesRepository).saveNote(any(Note.class)); // saved to the model
//        verify(mAddNoteView).showNotesList(); // shown in the UI
//    }



}
