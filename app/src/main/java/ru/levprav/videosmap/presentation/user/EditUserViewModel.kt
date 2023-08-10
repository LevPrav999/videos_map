package ru.levprav.videosmap.presentation.user

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.levprav.videosmap.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

}