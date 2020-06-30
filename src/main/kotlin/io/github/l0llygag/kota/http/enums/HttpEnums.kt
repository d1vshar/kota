package io.github.l0llygag.kota.http.enums

/**
 * Enum for different HTTP headers.
 *
 * @param headersKeys string of the header in camel case.
 */
enum class HttpHeader(val headersKeys: String) {
    ACCEPT("Accept"),
    ACCEPT_CHARSET("Accept-Charset"),
    ACCEPT_ENCODING("Accept-Encoding"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type")
}

/**
 * Enum for different HTTP status codes.
 *
 * @param code The HTTP status code.
 * @param description Description of the code.
 */
enum class HttpStatus(val code: Int, val description: String) {
    CONTINUE(100,	"Continue"),
    OK(200,	"OK"),
    ACCEPTED(202,	"Accepted"),
    FOUND(302,	"Found"),
    BAD_REQUEST(400,	"Bad Request"),
    UNAUTHORIZED(401,	"Unauthorized"),
    FORBIDDEN(403,	"Forbidden"),
    NOT_FOUND(404,	"Not Found"),
    METHOD_NOT_ALLOWED(405,	"Method Not Allowed"),
    URI_TOO_LONG(414,	"URI Too Long"),
    INTERNAL_SERVER_ERROR(500,	"Internal io.github.l0llygag.kota.Server Error"),
    NOT_IMPLEMENTED(501,	"Not Implemented"),
    HTTP_VERSION_NOT_SUPPORTED(505,	"HTTP Version Not Supported"),
    UNDECIDED(-1,"Request Not Validated")
}

/**
 * Enum for different HTTP versions.
 *
 * @param version string value of the version
 */
enum class HttpVersion(val version: String) {
    HTTP_1_1("HTTP/1.1"),
    UNKNOWN("null")
}

/**
 * Enum for different HTTP status codes.
 */
enum class HttpMethod {
    GET,
    UNKNOWN
}

/**
 * Enum for different mime-types for [io.github.l0llygag.kota.http.enums.HttpHeader.CONTENT_TYPE] header.
 *
 * @param ext File extension to be matched for the mime-type.
 * @param mime The mime-type string value.
 * @param charset Charset to appended to header for the mime-type. `null` is exception for no charset required.
 */
enum class MimeType(val ext: String, val mime: String, val charset: String) {
    AAC(".aac", "audio/aac","null"),
    ABW(".abw", "application/x-abiword","null"),
    ARC(".arc", "application/x-freearc","null"),
    AVI(".avi", "video/x-msvideo","null"),
    AZW(".azw", "application/vnd.amazon.ebook","null"),
    BIN(".bin", "application/octet-stream","null"),
    BMP(".bmp", "image/bmp","null"),
    BZ(".bz", "application/x-bzip","null"),
    BZ2(".bz2", "application/x-bzip2","null"),
    CSH(".csh", "application/x-csh","null"),
    CSS(".css", "text/css","utf-8"),
    CSV(".csv", "text/csv","utf-8"),
    DOC(".doc", "application/msword","null"),
    DOCX(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document","null"),
    EOT(".eot", "application/vnd.ms-fontobject","null"),
    EPUB(".epub", "application/epub+zip","null"),
    GZ(".gz", "application/gzip","null"),
    GIF(".gif", "image/gif","null"),
    HTM(".htm", "text/html","utf-8"),
    HTML(".html", "text/html","utf-8"),
    ICO(".ico", "image/vnd.microsoft.icon","null"),
    ICS(".ics", "text/calendar","utf-8"),
    JAR(".jar", "application/java-archive","null"),
    JPEG(".jpeg", "image/jpeg","null"),
    JPG(".jpg", "image/jpeg","null"),
    JS(".js", "text/javascript","utf-8"),
    JSON(".json", "application/json","utf-8"),
    JSONLD(".jsonld", "application/ld+json","null"),
    MID(".mid", "audio/midi audio/x-midi","null"),
    MIDI(".midi", "audio/midi audio/x-midi","null"),
    MJS(".mjs", "text/javascript","utf-8"),
    MP3(".mp3", "audio/mpeg","null"),
    MPEG(".mpeg", "video/mpeg","null"),
    MPKG(".mpkg", "application/vnd.apple.installer+xml","null"),
    ODP(".odp", "application/vnd.oasis.opendocument.presentation","null"),
    ODS(".ods", "application/vnd.oasis.opendocument.spreadsheet","null"),
    ODT(".odt", "application/vnd.oasis.opendocument.text","null"),
    OGA(".oga", "audio/ogg","null"),
    OGV(".ogv", "video/ogg","null"),
    OGX(".ogx", "application/ogg","null"),
    OPUS(".opus", "audio/opus","null"),
    OTF(".otf", "font/otf","null"),
    PNG(".png", "image/png","null"),
    PDF(".pdf", "application/pdf","null"),
    PHP(".php", "application/x-httpd-php","null"),
    PPT(".ppt", "application/vnd.ms-powerpoint","null"),
    PPTX(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation","null"),
    RAR(".rar", "application/vnd.rar","null"),
    RTF(".rtf", "application/rtf","null"),
    SH(".sh", "application/x-sh","null"),
    SVG(".svg", "image/svg+xml","null"),
    SWF(".swf", "application/x-shockwave-flash","null"),
    TAR(".tar", "application/x-tar","null"),
    TIF(".tif", "image/tiff","null"),
    TIFF(".tiff", "image/tiff","null"),
    TS(".ts", "video/mp2t","null"),
    TTF(".ttf", "font/ttf","null"),
    TXT(".txt", "text/plain","utf-8"),
    VSD(".vsd", "application/vnd.visio","null"),
    WAV(".wav", "audio/wav","null"),
    WEBA(".weba", "audio/webm","null"),
    WEBM(".webm", "video/webm","null"),
    WEBP(".webp", "image/webp","null"),
    WOFF(".woff", "font/woff","null"),
    WOFF2(".woff2", "font/woff2","null"),
    XHTML(".xhtml", "application/xhtml+xml","null"),
    XLS(".xls", "application/vnd.ms-excel","null"),
    XLSX(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","null"),
    XML(".xml", "application/xml","null"),
    XUL(".xul", "application/vnd.mozilla.xul+xml","null"),
    ZIP(".zip", "application/zip","null"),
    F3GP(".3gp", "video/3gpp","null"),
    F3G2(".3g2", "video/3gpp2","null"),
    F7Z(".7z", "application/x-7z-compressed","null"),
    UNKNOWN(".*","application/octet-stream", "nul") // RFC2046, Section 4.5.1
}