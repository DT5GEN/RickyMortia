package com.dt5gen.rickymortia.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dt5gen.rickymortia.data.local.CharacterEntity

/**
 * Карточка персонажа:

 */
@Composable
fun CharacterCard(
    item: CharacterEntity,
    onClick: () -> Unit,
    onLike: (CharacterEntity) -> Unit,
    onStudied: (CharacterEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),

    ) {
        Column {

            // ---------- Верхняя часть с изображением ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                val imageUrl = item.imageUrl.orEmpty()

                if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }

                // Бейдж «изучено»
                StudiedCapBadge(
                    studied = item.isStudied,
                    onClick = { onStudied(item) },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 6.dp, top = 6.dp)
                )
            }

            // ---------- Нижняя тёмная компактная плашка ----------
            FooterBlock(
                name = item.name,
                species = "${item.gender} | ${item.species}",
                status = item.status ?: "Unknown",
                liked = item.isFavorite,
                onLike = { onLike(item) }
            )
        }
    }
}

/* ======================= Внутренние компоненты ======================= */
@Composable
private fun FooterBlock(
    name: String,
    species: String,
    status: String,
    liked: Boolean,
    onLike: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = species,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            FavoriteHeart(
                liked = liked,
                onClick = onLike,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        // Статус — снизу по центру, прозрачный фон, мелкий шрифт
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            StatusPillCentered(text = status)
        }
    }
}

/** Прозрачный бейдж «изучено» — только иконка */
@Composable
private fun StudiedCapBadge(
    studied: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Icons.Outlined.School,
        contentDescription = if (studied) "Studied" else "Mark as studied",
        tint = if (studied) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .size(22.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = onClick
            )
    )
}

/** Сердце «избранное» — кликабельное */
@Composable
private fun FavoriteHeart(
    liked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = if (liked) "Unlike" else "Like",
        tint = if (liked) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .size(22.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = onClick
            )
    )
}

/** Прозрачный (без подложки) статус внизу карточки, шрифт поменьше */
@Composable
private fun StatusPillCentered(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    when (text.lowercase()) {
                        "alive" -> Color(0xFF34D399) // зелёная точка
                        "dead"  -> MaterialTheme.colorScheme.error
                        else    -> MaterialTheme.colorScheme.outline
                    }
                )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}