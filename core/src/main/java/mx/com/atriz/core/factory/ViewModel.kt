package mx.com.atriz.core.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * [Action]s are inputs into ViewModel
 *
 * [Effect]s are asynchronous tasks that are automatically run on a background thread. Upon completion of the [Effect], the processor
 * should emit an [Action] that will either modify the [State] or emit an [Event] (or perhaps do nothing if the Action is no-op)
 *
 * [Event]s are a way to synchronously (on UI thread) communicate transient information that shouldn't be kept as part of the [State]
 *
 * [State] is a bundle of data that represents the current state of this [ViewModel].
 *
 */
abstract class ViewModel<Action : Any, Effect : Any, State : Any, Event : Any>(
    initialState: State,
    initialEffects: Set<Effect> = emptySet(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _event = Channel<Event>(Channel.CONFLATED)
    val event: Flow<Event> = _event.receiveAsFlow()

    init {
        addEffects(initialEffects)
    }

    /**
     * Process an [Action] and return a new [State], it can launch an [Effect] or emit an [Event]
     */
    fun action(action: Action) = viewModelScope.launch(dispatcher) {
        _state.value = reduxActions(_state.value, action)
    }

    /**
     * Process an [Effect] and return an [Action] to be processed
     */
    fun addEffect(effect: Effect) = viewModelScope.launch(dispatcher) {
        reduxEffects(effect)?.let { action(it) }
    }

    /**
     * Process a set of [Effect]s
     */
    fun addEffects(effects: Set<Effect>) = viewModelScope.launch(dispatcher) {
        effects.forEach { addEffect(it) }
    }

    /**
     * Emit an [Event]
     */
    fun addEvent(event: Event) = viewModelScope.launch(dispatcher) {
        _event.send(event)
    }

    /**
     * Implement this method to update the [State] based on an [Action]
     */
    abstract suspend fun reduxActions(currentState: State, action: Action): State

    /**
     * Implement this method to process an [Action] from an [Effect]
     */
    abstract suspend fun reduxEffects(effect: Effect): Action?
}
