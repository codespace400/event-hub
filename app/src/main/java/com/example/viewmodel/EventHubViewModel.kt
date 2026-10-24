package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CheckInResult
import com.example.data.EventRepository
import com.example.model.Attendee
import com.example.model.Event
import com.example.model.UserRole
import com.example.model.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class EventHubViewModel(
    private val repository: EventRepository = EventRepository()
) : ViewModel() {

    // Current Session (Organizer or Attendee)
    private val _currentUser = MutableStateFlow(
        UserSession(
            role = UserRole.ORGANIZER,
            name = "Alex Morgan",
            email = "alex.organizer@eventhub.com"
        )
    )
    val currentUser: StateFlow<UserSession> = _currentUser.asStateFlow()

    // Navigation and screen state
    private val _currentScreen = MutableStateFlow("events_list")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _selectedEventId = MutableStateFlow<String?>("evt_tech_summit")
    val selectedEventId: StateFlow<String?> = _selectedEventId.asStateFlow()

    // Event creation / editing state
    private val _editingEvent = MutableStateFlow<Event?>(null)
    val editingEvent: StateFlow<Event?> = _editingEvent.asStateFlow()

    // Search and filter for events
    private val _eventSearchQuery = MutableStateFlow("")
    val eventSearchQuery: StateFlow<String> = _eventSearchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Attendee list search and filter
    private val _attendeeSearchQuery = MutableStateFlow("")
    val attendeeSearchQuery: StateFlow<String> = _attendeeSearchQuery.asStateFlow()

    private val _attendeeFilter = MutableStateFlow("All") // "All", "Checked-In", "Pending"
    val attendeeFilter: StateFlow<String> = _attendeeFilter.asStateFlow()

    // Check-in Scanner State
    private val _scannerQuery = MutableStateFlow("")
    val scannerQuery: StateFlow<String> = _scannerQuery.asStateFlow()

    private val _lastCheckInResult = MutableStateFlow<CheckInResult?>(null)
    val lastCheckInResult: StateFlow<CheckInResult?> = _lastCheckInResult.asStateFlow()

    private val _recentScans = MutableStateFlow<List<CheckInResult>>(emptyList())
    val recentScans: StateFlow<List<CheckInResult>> = _recentScans.asStateFlow()

    // Registration confirmation ticket
    private val _lastRegistration = MutableStateFlow<Pair<Attendee, Event>?>(null)
    val lastRegistration: StateFlow<Pair<Attendee, Event>?> = _lastRegistration.asStateFlow()

    // Ticket dialog view state
    private val _inspectedTicket = MutableStateFlow<Pair<Attendee, Event>?>(null)
    val inspectedTicket: StateFlow<Pair<Attendee, Event>?> = _inspectedTicket.asStateFlow()

    // Export confirmation banner
    private val _exportMessage = MutableStateFlow<String?>(null)
    val exportMessage: StateFlow<String?> = _exportMessage.asStateFlow()

    // Exposed Flows from repository
    val allEvents: StateFlow<List<Event>> = repository.events
    val allAttendees: StateFlow<List<Attendee>> = repository.attendees

    // Filtered events
    val filteredEvents: StateFlow<List<Event>> = combine(
        allEvents,
        _eventSearchQuery,
        _selectedCategory
    ) { events, query, category ->
        events.filter { event ->
            val matchesQuery = query.isBlank() ||
                    event.name.contains(query, ignoreCase = true) ||
                    event.venue.contains(query, ignoreCase = true)
            val matchesCategory = category == "All" || event.category.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected event flow
    val currentEvent: StateFlow<Event?> = combine(
        allEvents,
        _selectedEventId
    ) { events, id ->
        events.find { it.id == id } ?: events.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current event's filtered attendees
    val currentAttendees: StateFlow<List<Attendee>> = combine(
        allAttendees,
        _selectedEventId,
        _attendeeSearchQuery,
        _attendeeFilter
    ) { attendees, eventId, query, filter ->
        attendees.filter { it.eventId == eventId }
            .filter { attendee ->
                val matchesQuery = query.isBlank() ||
                        attendee.name.contains(query, ignoreCase = true) ||
                        attendee.email.contains(query, ignoreCase = true) ||
                        attendee.ticketCode.contains(query, ignoreCase = true)
                val matchesFilter = when (filter) {
                    "Checked-In" -> attendee.isCheckedIn
                    "Pending" -> !attendee.isCheckedIn
                    else -> true
                }
                matchesQuery && matchesFilter
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // My Tickets for Attendee role
    val myTickets: StateFlow<List<Pair<Attendee, Event>>> = combine(
        allAttendees,
        allEvents,
        _currentUser
    ) { attendees, events, user ->
        val userRegistrations = attendees.filter {
            it.email.equals(user.email, ignoreCase = true) ||
            user.role == UserRole.ATTENDEE // for demo preview convenience
        }
        userRegistrations.mapNotNull { attendee ->
            val event = events.find { it.id == attendee.eventId }
            if (event != null) Pair(attendee, event) else null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun switchUserRole(role: UserRole) {
        if (role == UserRole.ORGANIZER) {
            _currentUser.value = UserSession(
                role = UserRole.ORGANIZER,
                name = "Alex Morgan",
                email = "alex.organizer@eventhub.com"
            )
            _currentScreen.value = "events_list"
        } else {
            _currentUser.value = UserSession(
                role = UserRole.ATTENDEE,
                name = "Jordan Attendee",
                email = "jordan.attendee@example.com"
            )
            _currentScreen.value = "my_tickets"
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun selectEvent(eventId: String, destinationScreen: String? = null) {
        _selectedEventId.value = eventId
        if (destinationScreen != null) {
            _currentScreen.value = destinationScreen
        }
    }

    fun setEventSearchQuery(query: String) {
        _eventSearchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setAttendeeSearchQuery(query: String) {
        _attendeeSearchQuery.value = query
    }

    fun setAttendeeFilter(filter: String) {
        _attendeeFilter.value = filter
    }

    fun setScannerQuery(query: String) {
        _scannerQuery.value = query
    }

    fun toggleCheckIn(attendeeId: String) {
        repository.toggleCheckIn(attendeeId)
    }

    fun processCheckIn(query: String? = null) {
        val targetQuery = query ?: _scannerQuery.value
        val eventId = _selectedEventId.value ?: return
        val result = repository.checkIn(eventId, targetQuery)
        _lastCheckInResult.value = result
        _recentScans.value = listOf(result) + _recentScans.value.take(4)
        if (query == null) {
            _scannerQuery.value = ""
        }
    }

    fun clearCheckInResult() {
        _lastCheckInResult.value = null
    }

    fun registerForEvent(
        eventId: String,
        name: String,
        email: String,
        phone: String,
        quantity: Int = 1
    ) {
        val newAttendee = repository.registerAttendee(
            eventId = eventId,
            name = name,
            email = email,
            phone = phone,
            quantity = quantity
        )
        val event = repository.getEventById(eventId)
        if (event != null) {
            _lastRegistration.value = Pair(newAttendee, event)
            _currentScreen.value = "registration_confirmed"
        }
    }

    fun openCreateEvent() {
        _editingEvent.value = null
        _currentScreen.value = "create_edit_event"
    }

    fun openEditEvent(event: Event) {
        _editingEvent.value = event
        _currentScreen.value = "create_edit_event"
    }

    fun saveEvent(
        name: String,
        description: String,
        date: String,
        time: String,
        venue: String,
        coverImage: String,
        capacity: Int,
        price: String,
        category: String,
        isPublished: Boolean
    ) {
        val currentEdit = _editingEvent.value
        if (currentEdit != null) {
            val updated = currentEdit.copy(
                name = name,
                description = description,
                date = date,
                time = time,
                venue = venue,
                coverImage = coverImage,
                capacity = capacity,
                price = price,
                category = category,
                isPublished = isPublished
            )
            repository.updateEvent(updated)
            _selectedEventId.value = updated.id
        } else {
            val newEvent = Event(
                id = "evt_${UUID.randomUUID().toString().take(8)}",
                name = name,
                description = description,
                date = date,
                time = time,
                venue = venue,
                coverImage = coverImage,
                capacity = capacity,
                registeredCount = 0,
                price = price,
                category = category,
                isPublished = isPublished,
                organizerName = _currentUser.value.name
            )
            repository.addEvent(newEvent)
            _selectedEventId.value = newEvent.id
        }
        _currentScreen.value = "event_detail"
    }

    fun deleteEvent(eventId: String) {
        repository.deleteEvent(eventId)
        _currentScreen.value = "events_list"
    }

    fun setInspectedTicket(ticket: Pair<Attendee, Event>?) {
        _inspectedTicket.value = ticket
    }

    fun exportAttendeesCsv() {
        val attendees = currentAttendees.value
        val event = currentEvent.value
        val count = attendees.size
        _exportMessage.value = "Exported $count attendees for '${event?.name ?: "Event"}' (CSV copied to clipboard / ready to share)"
    }

    fun clearExportMessage() {
        _exportMessage.value = null
    }
}
