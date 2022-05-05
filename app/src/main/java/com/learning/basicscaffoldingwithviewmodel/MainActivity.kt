package com.learning.basicscaffoldingwithviewmodel
//======================================================================
// BASIC SCAFFOLDING WITH VIEW MODEL
//	Created: 3 May 2022 by Jason
//	Purpose: Basic, Customizable Layout for Most Projects
//	Inspiration: https://www.rockandnull.com/jetpack-compose-viewmodel/
//	Dependencies:
//		* ADD implementation "androidx.compose.runtime:runtime-livedata:1.2.0-alpha08"
//======================================================================

//----------------------------------------------------------------------
// Imports
//----------------------------------------------------------------------
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.learning.basicscaffoldingwithviewmodel.ui.theme.BasicScaffoldingwithViewModelTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//----------------------------------------------------------------------
// View Model
//----------------------------------------------------------------------
class MainViewModel : ViewModel() {

	val counterLiveDate: LiveData<Int>
		get() = counter

	private var count = 0							// Operate on Count
	private val counter = MutableLiveData<Int>()	// Observe Counter as Live Data

	fun increaseCounter() {
		counter.value = ++count
		println("Increase Counter to " + counter.value.toString())
	}

	fun changeCounter(newValue: Int) {
		count = newValue
		counter.value = count
		println("Change Counter to " + counter.value.toString())
	}
}

//----------------------------------------------------------------------
// Main Activity
//----------------------------------------------------------------------
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			BasicScaffoldingwithViewModelTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					SetupScaffold()
				}
			}
		}
	}
}

//----------------------------------------------------------------------
// Setup Scaffold
//----------------------------------------------------------------------
@Composable
fun SetupScaffold(model: MainViewModel = MainViewModel()) {
	val scope = rememberCoroutineScope()
	val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
	val count by model.counterLiveDate.observeAsState(0)
	Scaffold(
		scaffoldState = scaffoldState,
		topBar = { ScaffoldTopBar(scope, scaffoldState) },
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = { ScaffoldFloatingActionButton(model, count) },
		drawerContent = { ScaffoldDrawerContent(model, count) },
		drawerGesturesEnabled = true,
		content = { Content(model, count) },
		bottomBar = { ScaffoldBottomBar() }
	)
}

//----------------------------------------------------------------------
// Content
//----------------------------------------------------------------------
@Composable
fun Content(
	model: MainViewModel = MainViewModel(),
	count: Int
) {
	Column(Modifier.fillMaxSize()) {
		Spacer(Modifier.height(10.dp))
		Text("View Model Count = $count")
		Divider()
		Button(
			onClick = { model.increaseCounter() },
		) {
			Text(text = "Add 1")
		}
	}
}

//----------------------------------------------------------------------
// Top Bar
//----------------------------------------------------------------------
@Composable
fun ScaffoldTopBar(
	scope: CoroutineScope,
	scaffoldState: ScaffoldState
) {
	TopAppBar(
		title = {
			Text(text = "Basic Scaffold")
		},
		navigationIcon = {
			IconButton(
				onClick = {
					scope.launch {
						scaffoldState.drawerState.open()
					}
				},
			) {
				Icon(
					Icons.Rounded.Menu,
					contentDescription = "Open Menu Drawer"
				)
			}
		})
}

//----------------------------------------------------------------------
// Drawer
//----------------------------------------------------------------------
@Composable
fun ScaffoldDrawerContent(
	model: MainViewModel = MainViewModel(),
	count: Int
) {
	ClickableText(
		text = AnnotatedString("Reset Counter"),
		style = TextStyle(color = Blue, fontSize = 20.sp),
		onClick = {
			model.changeCounter(0)
		}
	)
}

//----------------------------------------------------------------------
// Floating Action Button
//----------------------------------------------------------------------
@Composable
fun ScaffoldFloatingActionButton(
	model: MainViewModel = MainViewModel(),
	count: Int
) {
	FloatingActionButton(onClick = {
		model.increaseCounter()
	}){
		IconButton(onClick = {
			model.increaseCounter()		// Doesn't Work
		}) {
			Icon(
				imageVector = Icons.Default.Add,
				contentDescription = "Floating Action Button",
			)
		}
	}
}

//----------------------------------------------------------------------
// Scaffold Bottom Bar
//----------------------------------------------------------------------
@Composable
fun ScaffoldBottomBar() {
	BottomAppBar(
		backgroundColor = MaterialTheme.colors.primary
	) {
		Text("Bottom App Bar")
	}
}