package eu.konopski

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import jakarta.ws.rs.Path as EndpointPath
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

import org.jboss.resteasy.reactive.server.multipart.FormValue
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput

import java.nio.file.Files
import java.nio.file.Paths

@EndpointPath("/hello")
class Hello {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  String hello() { "HELLO FROM APP" }


}


@Slf4j
@EndpointPath("/upload")
class UploadEndpoint {


    @POST
    Response upload(MultipartFormDataInput input) {

        var metadata = metaDataIfNotFile(input.values)

        var files = extractFiles(input.values, metadata)

        files
            .forEach { write(it) }

        Response.status(201).entity(metadata).build()

    }


    def write(DataItem data) {
        var uploads = Paths.get("./my-uploads")
        var path = uploads.resolve(data.getFilename())
        try {
            if(!Files.exists(uploads)) {
                Files.createDirectories(uploads)
            }
            Files.write(path, data.data)
        } catch (e) {
            e.printStackTrace()
        }
    }


    static def metaDataIfNotFile(Map<String, Collection<FormValue>> formData) {
        formData
            .findAll { it.value.every { !it.fileItem } }
            .collectEntries { k,  formValues ->
                [ (k): formValues.collect { it.value } ] }
    }


    def extractFiles(Map<String, Collection<FormValue>> values, metadata) {
            values
                .findAll { it.value.find { it.fileItem } }
                .collectMany { extractData(it, metadata) }
    }

    def extractData(Map.Entry<String, Collection<FormValue>> entry, meta) {
            entry.value
                .findAll { it.fileItem && it.fileName?.trim() }
                .collectMany { tryData(it, meta) }
    }

    def tryData(FormValue formValue, meta) {
        try {
            [
                [
                    data    : Files.readAllBytes(formValue.getFileItem().file),
                    filename: formValue.fileName,
                    metaData: meta
                ] as DataItem
            ]
        } catch (e) {
            e.printStackTrace()
           []
        }
    }
}

@ToString @EqualsAndHashCode
class DataItem {
    byte[] data
    String filename
    Map<String, Collection<String>> metaData
}
