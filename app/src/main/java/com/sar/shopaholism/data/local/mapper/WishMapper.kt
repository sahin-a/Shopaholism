package com.sar.shopaholism.data.local.mapper

import com.sar.shopaholism.data.local.entity.WishEntity
import com.sar.shopaholism.domain.entity.Wish

/* Wish Entity -> Wish */

private fun map(wishEntity: WishEntity) =
    Wish(id = wishEntity.id, title = wishEntity.title, priority = wishEntity.priority)

fun WishEntity.toWish() = map(this)
fun List<WishEntity>.toWishes() = map { it.toWish() }

/* Wish -> Wish Entity */

private fun map(wish: Wish) =
    WishEntity(id = wish.id, title = wish.title, priority = wish.priority)

fun Wish.toWishEntity() = map(this)
fun List<Wish>.toWishEntities() = map { it.toWishEntity() }
