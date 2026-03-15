package com.popop.lifesimulator.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popop.lifesimulator.core.navigation.NavigationAction
import com.popop.lifesimulator.core.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing navigation state.
 * Uses SharedFlow to emit navigation events to the UI.
 */
@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {
    
    private val _navigationActions = MutableSharedFlow<NavigationAction>()
    val navigationActions: SharedFlow<NavigationAction> = _navigationActions.asSharedFlow()
    
    /**
     * Navigate to a specific screen.
     */
    fun navigate(screen: Screen) {
        viewModelScope.launch {
            _navigationActions.emit(NavigationAction.Navigate(screen))
        }
    }
    
    /**
     * Navigate to a screen and clear the back stack.
     */
    fun navigateAndClearBackStack(screen: Screen) {
        viewModelScope.launch {
            _navigationActions.emit(NavigationAction.NavigateAndClearBackStack(screen))
        }
    }
    
    /**
     * Pop back a specified number of screens.
     */
    fun popBack(count: Int = 1) {
        viewModelScope.launch {
            _navigationActions.emit(NavigationAction.PopBack(count))
        }
    }
    
    /**
     * Pop back to the start destination.
     */
    fun popToStart() {
        viewModelScope.launch {
            _navigationActions.emit(NavigationAction.PopToStart)
        }
    }
}
