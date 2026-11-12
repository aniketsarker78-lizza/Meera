package com.example

import com.example.service.OwnerVoiceVerifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testWakeWordRegexMatches() {
    val verifier = OwnerVoiceVerifier()
    assertTrue(verifier.matchesWakeWordRegex("Hey Meera"))
    assertTrue(verifier.matchesWakeWordRegex("hey meera how are you"))
    assertTrue(verifier.matchesWakeWordRegex("হে মীরা কেমন আছো"))
    assertTrue(verifier.matchesWakeWordRegex("হেই মীরা"))
  }

  @Test
  fun testWakeWordRegexRejectsFalseTriggers() {
    val verifier = OwnerVoiceVerifier()
    assertFalse(verifier.matchesWakeWordRegex("hey"))
    assertFalse(verifier.matchesWakeWordRegex("kai"))
    assertFalse(verifier.matchesWakeWordRegex("he"))
    assertFalse(verifier.matchesWakeWordRegex("oi"))
    assertFalse(verifier.matchesWakeWordRegex("hello there"))
  }

  @Test
  fun testOwnerVoiceEnrollment() {
    val verifier = OwnerVoiceVerifier()
    assertEquals(0, verifier.samplesCollected.value)
    assertFalse(verifier.isEnrolled.value)

    verifier.addEnrollmentSample()
    assertEquals(1, verifier.samplesCollected.value)

    verifier.addEnrollmentSample()
    assertEquals(2, verifier.samplesCollected.value)

    verifier.addEnrollmentSample()
    assertEquals(3, verifier.samplesCollected.value)
    assertTrue(verifier.isEnrolled.value)

    // Verification check with wake word
    val verified = verifier.verifySpeaker("Hey Meera")
    assertTrue(verified)
  }
}
