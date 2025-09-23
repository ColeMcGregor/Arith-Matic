package com.colemcg.arith_matic.utils

import java.util.UUID


/**
 * Simple, centralized ID helpers.
 * made here to change easily the format of the id creation logic
 * 
 * @author Cole McGrew
 * @version 1.0
 * @date 2025-09-08
 */
object Ids { //an object is a singleton, and globally accessible

    /** Standard 36-char UUID like "550e8400-e29b-41d4-a716-446655440000". */
    fun uuid(): String = UUID.randomUUID().toString()

    /**
     * Short, URL-friendly ID (base36) ~10 chars.
     * Combines time + randomness to reduce collision risk without heavy crypto.
     */
    fun shortId(random: kotlin.random.Random = RNG.default): String {
        val time = System.currentTimeMillis()
        val rand = random.nextLong()
        val mixed = time xor rand
        // Base36 & strip minus for compactness
        val a = time.toString(36)
        val b = mixed.toString(36).replace("-", "")
        // Keep it short & sweet
        return (a + b).takeLast(10)
    }
}
