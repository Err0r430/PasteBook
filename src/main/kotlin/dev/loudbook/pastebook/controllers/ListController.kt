package dev.loudbook.pastebook.controllers

import com.google.gson.Gson
import dev.loudbook.pastebook.BucketUtils
import dev.loudbook.pastebook.mongo.PasteRepository
import io.github.bucket4j.Bucket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ListController {
    @Autowired
    lateinit var pasteRepository: PasteRepository

    private val bucket: Bucket = BucketUtils.getBucketPerSeconds(4)

    @GetMapping("/list")
    fun list(): ResponseEntity<String> {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(429).body("Rate limit exceeded")
        }

        val pastes = pasteRepository.findAllDTO()
            .filter { it.created < System.currentTimeMillis() }
            .filterNot { it.unlisted }
            .take(80)
            .toList()

        val json = Gson().toJsonTree(pastes).asJsonArray

        json.mapIndexed { _, element ->
            element.asJsonObject.remove("content")
        }

        return ResponseEntity.ok(json.toString())
    }
}