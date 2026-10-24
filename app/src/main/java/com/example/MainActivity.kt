package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.UserRole
import com.example.ui.screens.AttendeeListScreen
import com.example.ui.screens.CheckInScannerScreen
import com.example.ui.screens.CreateEditEventScreen
import com.example.ui.screens.EventDetailScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MyTicketsScreen
import com.example.ui.screens.OrganizerEventsScreen
import com.example.ui.screens.PublicEventsScreen
import com.example.ui.screens.RegisterEventScreen
import com.example.ui.theme.EventHubTheme
import com.example.viewmodel.EventHubViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventHubTheme {
                EventHubApp()
            }
        }
    }
}

@Composable
fun EventHubApp(
    viewModel: EventHubViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val events by viewModel.filteredEvents.collectAsState()
    val allEvents by viewModel.allEvents.collectAsState()
    val currentEvent by viewModel.currentEvent.collectAsState()
    val editingEvent by viewModel.editingEvent.collectAsState()
    val currentAttendees by viewModel.currentAttendees.collectAsState()
    val myTickets by viewModel.myTickets.collectAsState()
    val eventSearchQuery by viewModel.eventSearchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val attendeeSearchQuery by viewModel.attendeeSearchQuery.collectAsState()
    val attendeeFilter by viewModel.attendeeFilter.collectAsState()
    val scannerQuery by viewModel.scannerQuery.collectAsState()
    val lastCheckInResult by viewModel.lastCheckInResult.collectAsState()
    val recentScans by viewModel.recentScans.collectAsState()
    val lastRegistration by viewModel.lastRegistration.collectAsState()
    val exportBannerMessage by viewModel.exportMessage.collectAsState()

    // Back handling for navigation stack
    BackHandler(enabled = currentScreen != "login") {
        when (currentScreen) {
            "events_list", "my_tickets" -> viewModel.navigateTo("login")
            "create_edit_event" -> {
                if (currentEvent != null) viewModel.navigateTo("event_detail")
                else viewModel.navigateTo("events_list")
            }
            "event_detail" -> viewModel.navigateTo("events_list")
            "attendee_list" -> viewModel.navigateTo("event_detail")
            "check_in_scanner" -> viewModel.navigateTo("event_detail")
            "register_event", "registration_confirmed" -> {
                if (currentUser.role == UserRole.ORGANIZER) viewModel.navigateTo("event_detail")
                else viewModel.navigateTo("public_events")
            }
            "public_events" -> viewModel.navigateTo("my_tickets")
            else -> viewModel.navigateTo("login")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .navigationBarsPadding()
    ) {
        when (currentScreen) {
            "login" -> {
                LoginScreen(
                    onLoginSuccess = { role ->
                        viewModel.switchUserRole(role)
                    }
                )
            }

            "events_list" -> {
                OrganizerEventsScreen(
                    events = events,
                    userSession = currentUser,
                    searchQuery = eventSearchQuery,
                    selectedCategory = selectedCategory,
                    onSearchChange = { viewModel.setEventSearchQuery(it) },
                    onCategoryChange = { viewModel.setSelectedCategory(it) },
                    onEventClick = { event ->
                        viewModel.selectEvent(event.id, "event_detail")
                    },
                    onCreateEventClick = {
                        viewModel.openCreateEvent()
                    },
                    onSwitchRole = {
                        viewModel.switchUserRole(UserRole.ATTENDEE)
                    },
                    onOpenCheckInScanner = {
                        if (events.isNotEmpty()) {
                            viewModel.selectEvent(events.first().id, "check_in_scanner")
                        }
                    }
                )
            }

            "create_edit_event" -> {
                CreateEditEventScreen(
                    editingEvent = editingEvent,
                    onSave = { name, desc, date, time, venue, cover, cap, price, cat, pub ->
                        viewModel.saveEvent(name, desc, date, time, venue, cover, cap, price, cat, pub)
                    },
                    onBack = {
                        if (currentEvent != null) viewModel.navigateTo("event_detail")
                        else viewModel.navigateTo("events_list")
                    }
                )
            }

            "event_detail" -> {
                if (currentEvent != null) {
                    EventDetailScreen(
                        event = currentEvent!!,
                        attendees = currentAttendees,
                        onBack = { viewModel.navigateTo("events_list") },
                        onViewAttendees = { viewModel.navigateTo("attendee_list") },
                        onOpenCheckIn = { viewModel.navigateTo("check_in_scanner") },
                        onEditEvent = { evt -> viewModel.openEditEvent(evt) },
                        onDeleteEvent = { evtId -> viewModel.deleteEvent(evtId) },
                        onOpenPublicPage = { evt ->
                            viewModel.selectEvent(evt.id, "register_event")
                        }
                    )
                } else {
                    viewModel.navigateTo("events_list")
                }
            }

            "register_event", "registration_confirmed" -> {
                if (currentEvent != null) {
                    RegisterEventScreen(
                        event = currentEvent!!,
                        lastRegistration = lastRegistration,
                        onRegister = { name, email, phone, quantity ->
                            viewModel.registerForEvent(currentEvent!!.id, name, email, phone, quantity)
                        },
                        onBack = {
                            if (currentUser.role == UserRole.ORGANIZER) {
                                viewModel.navigateTo("event_detail")
                            } else {
                                viewModel.navigateTo("public_events")
                            }
                        },
                        onGoToMyTickets = {
                            viewModel.switchUserRole(UserRole.ATTENDEE)
                        }
                    )
                } else {
                    viewModel.navigateTo("events_list")
                }
            }

            "attendee_list" -> {
                if (currentEvent != null) {
                    AttendeeListScreen(
                        event = currentEvent!!,
                        attendees = currentAttendees,
                        searchQuery = attendeeSearchQuery,
                        selectedFilter = attendeeFilter,
                        onSearchChange = { viewModel.setAttendeeSearchQuery(it) },
                        onFilterChange = { viewModel.setAttendeeFilter(it) },
                        onToggleCheckIn = { attendeeId -> viewModel.toggleCheckIn(attendeeId) },
                        onExport = { viewModel.exportAttendeesCsv() },
                        onOpenCheckInScanner = { viewModel.navigateTo("check_in_scanner") },
                        onBack = { viewModel.navigateTo("event_detail") },
                        exportBannerMessage = exportBannerMessage,
                        onDismissExportMessage = { viewModel.clearExportMessage() }
                    )
                } else {
                    viewModel.navigateTo("events_list")
                }
            }

            "check_in_scanner" -> {
                if (currentEvent != null) {
                    CheckInScannerScreen(
                        event = currentEvent!!,
                        attendees = currentAttendees,
                        scannerQuery = scannerQuery,
                        lastCheckInResult = lastCheckInResult,
                        recentScans = recentScans,
                        onQueryChange = { viewModel.setScannerQuery(it) },
                        onSubmitCheckIn = { query -> viewModel.processCheckIn(query) },
                        onClearResult = { viewModel.clearCheckInResult() },
                        onBack = { viewModel.navigateTo("event_detail") }
                    )
                } else {
                    viewModel.navigateTo("events_list")
                }
            }

            "my_tickets" -> {
                MyTicketsScreen(
                    tickets = myTickets,
                    userSession = currentUser,
                    onBrowseEvents = { viewModel.navigateTo("public_events") },
                    onSwitchRole = { viewModel.switchUserRole(UserRole.ORGANIZER) }
                )
            }

            "public_events" -> {
                PublicEventsScreen(
                    events = allEvents,
                    userSession = currentUser,
                    searchQuery = eventSearchQuery,
                    selectedCategory = selectedCategory,
                    onSearchChange = { viewModel.setEventSearchQuery(it) },
                    onCategoryChange = { viewModel.setSelectedCategory(it) },
                    onEventClick = { event ->
                        viewModel.selectEvent(event.id, "register_event")
                    },
                    onMyTicketsClick = { viewModel.navigateTo("my_tickets") },
                    onSwitchRole = { viewModel.switchUserRole(UserRole.ORGANIZER) }
                )
            }
        }
    }
}
