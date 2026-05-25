package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.TvChannel
import com.example.data.TvChannelProvider
import com.example.ui.MainViewModel
import com.example.ui.components.VideoPlayer
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainDashboard(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboard(viewModel: MainViewModel) {
    val context = LocalContext.current
    
    // Collecting states from the view model
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeChannel by viewModel.activeChannel.collectAsState()
    val filteredChannels by viewModel.filteredChannels.collectAsState(initial = emptyList())
    val isAboutOpened by viewModel.isAboutOpened.collectAsState()

    // Bento Grid Design Theme Colors from instructions
    val bentoBackground = Color(0xFFFDFBFF)
    val bentoTextPrimary = Color(0xFF1A1C1E)
    val bentoTextSecondary = Color(0xFF4A4D50)
    
    val bentoCardBackground = Color.White
    val bentoCardBorder = Color(0xFFF1F5F9)
    val bentoPrimaryBlue = Color(0xFF2563EB)
    
    val bentoIceBlue = Color(0xFFD3E4FF)
    val bentoIceBlueText = Color(0xFF001C38)
    val bentoIceBlueBadgeBg = Color(0xFFFFECEF)

    Scaffold(
        modifier = Modifier.fillMaxSize().background(bentoBackground),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = bentoPrimaryBlue,
                            modifier = Modifier
                                .size(12.dp)
                                .shadow(4.dp, CircleShape)
                        ) {}
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "SG CANLI TV",
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = 1.sp,
                            color = bentoTextPrimary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleAbout(true) },
                        modifier = Modifier
                            .testTag("about_button")
                            .padding(end = 4.dp)
                            .size(40.dp)
                            .background(
                                color = bentoPrimaryBlue.copy(alpha = 0.08f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = "Hakkında Bilgileri",
                            tint = bentoPrimaryBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = bentoBackground,
                    titleContentColor = bentoTextPrimary
                )
            )
        },
        containerColor = bentoBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Elegant Stream Player Panel or Bento Featured Box (Top Compartment)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                if (activeChannel != null) {
                    activeChannel?.let { channel ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .border(2.dp, bentoPrimaryBlue, RoundedCornerShape(24.dp))
                                .shadow(2.dp, RoundedCornerShape(24.dp))
                        ) {
                            VideoPlayer(
                                streamUrl = channel.streamUrl,
                                channelName = channel.name,
                                onClose = { viewModel.stopChannel() },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    // Main Featured Channel (Empty State / Hero Card) as request in Bento design
                    val trtChannel = TvChannelProvider.channels.firstOrNull { it.id == "trt1" }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(bentoPrimaryBlue, Color(0xFF1D4ED8))
                                )
                            )
                            .clickable {
                                trtChannel?.let { viewModel.playChannel(it) }
                            }
                            .padding(20.dp)
                    ) {
                        // CANLI pulsing style tag
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Color(0xFFEF4444), shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "CANLI",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Column(
                            modifier = Modifier.align(Alignment.BottomStart)
                        ) {
                            Text(
                                text = "ÖNE ÇIKAN YAYIN",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = trtChannel?.name ?: "TRT 1 HD",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Yüksek çözünürlüklü kesintisiz canlı yayın için dokunun",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Sleek Rounded Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("search_input"),
                placeholder = { Text("Kanal veya kategori ara...", color = bentoTextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Ara", tint = bentoPrimaryBlue) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Temizle", tint = bentoTextSecondary)
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = bentoPrimaryBlue,
                    unfocusedBorderColor = bentoCardBorder,
                    focusedTextColor = bentoTextPrimary,
                    unfocusedTextColor = bentoTextPrimary,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Category Select Row (Sleek Horizontal Scrollable Bento Pills)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TvChannelProvider.categories.forEach { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) bentoPrimaryBlue else Color.White
                                )
                                .border(1.dp, if (isSelected) bentoPrimaryBlue else bentoCardBorder, RoundedCornerShape(16.dp))
                                .clickable { viewModel.selectCategory(category) }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category,
                                color = if (isSelected) Color.White else bentoTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = 12.sp,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Channels Bento Grid Layout
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (filteredChannels.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tv,
                                    contentDescription = "Sonuç Yok",
                                    modifier = Modifier.size(56.dp),
                                    tint = bentoTextSecondary.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Aramanızla eşleşen kanal bulunamadı",
                                    color = bentoTextSecondary,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                } else {
                    items(filteredChannels, key = { it.id }) { channel ->
                        val isCurrentlyActive = activeChannel?.id == channel.id
                        
                        Card(
                            onClick = { viewModel.playChannel(channel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(108.dp)
                                .border(
                                    width = if (isCurrentlyActive) 2.dp else 1.dp,
                                    color = if (isCurrentlyActive) bentoPrimaryBlue else bentoCardBorder,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .testTag("channel_item_${channel.id}"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrentlyActive) bentoPrimaryBlue.copy(alpha = 0.05f) else bentoCardBackground
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Row showing logo icon cylinder and category badge
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Visual Initials Avatar Box
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .background(channel.composeColor, shape = RoundedCornerShape(10.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = channel.logoText.take(2),
                                                color = Color.White,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 12.sp
                                            )
                                        }

                                        // Category Badge
                                        Surface(
                                            color = bentoBackground,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = channel.category,
                                                color = bentoTextSecondary,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    // Title label and play trigger
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = channel.name,
                                                fontWeight = FontWeight.Bold,
                                                color = bentoTextPrimary,
                                                fontSize = 14.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = if (isCurrentlyActive) "Oynatılıyor..." else "Canlı İzle",
                                                color = if (isCurrentlyActive) bentoPrimaryBlue else bentoTextSecondary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Icon(
                                            imageVector = if (isCurrentlyActive) Icons.Rounded.PauseCircle else Icons.Rounded.PlayCircle,
                                            contentDescription = "Oynat",
                                            tint = bentoPrimaryBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Beautiful Inline Bento Card for Developer Info (Direct visual layout from theme mock HTML)
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(bentoIceBlue)
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Geliştirici Hakkında",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = bentoIceBlueText
                            )
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = "Hakkında",
                                    tint = bentoIceBlueText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Contacts Container Layout
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Name card
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.5f))
                                    .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Rounded.Badge, contentDescription = "İsim", tint = bentoPrimaryBlue, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("İsim Soyisim", color = bentoPrimaryBlue, fontSize = 9.sp, fontWeight = FontWeight.Black)
                                    Text("Serhat Güner", color = bentoTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Phone Card
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.5f))
                                    .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                                    .clickable {
                                        try {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:05428909804"))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {}
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Rounded.Call, contentDescription = "Ara", tint = bentoPrimaryBlue, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Telefon", color = bentoPrimaryBlue, fontSize = 9.sp, fontWeight = FontWeight.Black)
                                    Text("0542 890 98 04", color = bentoTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Mail Card
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.5f))
                                    .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                                    .clickable {
                                        try {
                                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                                data = Uri.parse("mailto:cezali.1genc@gmail.cim")
                                            }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {}
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Rounded.Mail, contentDescription = "E-Posta", tint = bentoPrimaryBlue, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("E-Posta", color = bentoPrimaryBlue, fontSize = 9.sp, fontWeight = FontWeight.Black)
                                    Text("cezali.1genc@gmail.cim", color = bentoTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Website anchor action
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(bentoPrimaryBlue)
                                    .clickable {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://modlu-1apk.xo.je"))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {}
                                    }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Rounded.Language, contentDescription = "Web", tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Web Sitemizi Ziyaret Edin",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Hakkında (About Details) Modal Dialog (Remains perfectly supported)
    if (isAboutOpened) {
        Dialog(
            onDismissRequest = { viewModel.toggleAbout(false) },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .wrapContentHeight()
                    .border(1.5.dp, bentoPrimaryBlue, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(4.dp, CircleShape)
                            .background(bentoPrimaryBlue, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SG",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Serhat Güner",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = bentoTextPrimary
                    )

                    Surface(
                        color = bentoIceBlue,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = "Uygulama Geliştiricisi & Editör",
                            color = bentoPrimaryBlue,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    HorizontalDivider(color = bentoCardBorder, modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(16.dp))

                    // Actionable Dialog contacts
                    AboutActionCard(
                        title = "Telefon",
                        value = "05428909804",
                        icon = Icons.Default.Phone,
                        color = Color(0xFF4CAF50),
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:05428909804"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Arama başarısız", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AboutActionCard(
                        title = "E-Posta",
                        value = "cezali.1genc@gmail.cim",
                        icon = Icons.Default.Email,
                        color = Color(0xFFFF9800),
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:cezali.1genc@gmail.cim")
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "E-posta uygulaması bulunamadı", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    AboutActionCard(
                        title = "Web Sitesi",
                        value = "https://modlu-1apk.xo.je",
                        icon = Icons.Default.Language,
                        color = Color(0xFF00BCD4),
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://modlu-1apk.xo.je"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Tarayıcı açılamadı", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.toggleAbout(false) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = bentoPrimaryBlue
                        )
                    ) {
                        Text(
                            text = "Kapat",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutActionCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFBFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = color.copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(text = title, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = value,
                        color = Color(0xFF1A1C1E),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Git",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

