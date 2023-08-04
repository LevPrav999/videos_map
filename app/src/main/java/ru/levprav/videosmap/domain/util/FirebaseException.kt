package ru.levprav.videosmap.domain.util

class FirebaseException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

}