package com.example.speechtotext

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {

    val voiceToTextParser by lazy {
        VoiceToTextParser(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var canRecord by remember {
                mutableStateOf(false)
            }
            val recordAudioLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    canRecord = isGranted
                }
            )
            LaunchedEffect(key1 = recordAudioLauncher) {
                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            val state by voiceToTextParser.state.collectAsState()
            var openDialog by rememberSaveable { mutableStateOf(false) }

            var listOfNumbers by remember {
                mutableStateOf(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
            }
            LaunchedEffect(key1 = state.isSpeaking) {
                if (state.isSpeaking) {
                    openDialog = true
                }

            }
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        if (state.isSpeaking) {
                            voiceToTextParser.stopListening()
                        } else {
                            voiceToTextParser.startListening()
                        }
                    }) {
                        AnimatedContent(targetState = state.isSpeaking, label = "") { isSpeaking ->
                            if (isSpeaking) {
                                Icon(imageVector = Icons.Rounded.Stop, contentDescription = null)
                            } else {
                                Icon(imageVector = Icons.Rounded.Mic, contentDescription = null)
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(50.dp), horizontalAlignment = Alignment.End
                    ) {
                        listOfNumbers.forEachIndexed { index, number ->
                            if (index == listOfNumbers.lastIndex) {
                                Row {
                                    Text(
                                        text = "+",
                                        fontSize = 20.sp,
                                        modifier = Modifier.wrapContentSize()
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = number.toString(),
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(top = 10.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = number.toString(),
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(top = 10.dp)
                                )
                            }
                        }
                        HorizontalDivider()
                        Row(modifier = Modifier.padding(top = 10.dp)) {
                            Text(text = "Toplam", fontSize = 30.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "${listOfNumbers.sum()}", fontSize = 30.sp)
                        }

                    }
                    if (openDialog) {
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .background(Color.Yellow)
                                .padding(20.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                AnimatedContent(
                                    targetState = state.isSpeaking,
                                ) { isSpeaking ->
                                    if (isSpeaking) {
                                        Text(text = "Dinliyorum")
                                    } else {
                                        Text(
                                            state.spokenText.ifEmpty { "Konuşmak için mikrofon simgesine tıklayın" },
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Button(onClick = {
                                    openDialog = false;
                                    voiceToTextParser.stopListening()
                                    listOfNumbers = listOfNumbers + state.spokenText.toInt()
                                }) {
                                    if (state.isSpeaking || state.spokenText.isEmpty()) {
                                        Text("Kapat")
                                    } else {
                                        Text("Ekle")
                                    }
                                }
                            }

                        }
                    }

                }

            }
        }
    }

}

