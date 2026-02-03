package com.digibo.core.controller;

import com.digibo.core.exception.ValidationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * BodiController - REST controller for document viewer operations
 * Maps to /bodi endpoints (NOT /api/bodi)
 */
@RestController
@RequestMapping("/bodi")
public class BodiController {

    /**
     * GET /bodi/show
     * Display document viewer HTML page
     *
     * Query params:
     * - docId: Document ID (required)
     */
    @GetMapping(value = "/show", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> showDocument(@RequestParam(required = false) String docId) {
        if (docId == null || docId.isEmpty()) {
            return ResponseEntity.badRequest().body("Document ID is required");
        }

        String html = """
                <!DOCTYPE html>
                <html>
                  <head>
                    <title>Document Viewer</title>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                  </head>
                  <body>
                    <div id="document-container">
                      <h1>Document ID: %s</h1>
                    </div>
                  </body>
                </html>
                """.formatted(docId);

        return ResponseEntity.ok(html);
    }
}
