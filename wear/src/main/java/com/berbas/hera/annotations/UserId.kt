package com.berbas.hera.annotations

import javax.inject.Qualifier

/**
 * The @UserId annotation is a custom qualifier annotation that is used to annotate
 * the injection of the user ID into classes
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserId