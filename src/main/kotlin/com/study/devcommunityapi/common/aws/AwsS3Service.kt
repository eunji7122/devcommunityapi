package com.study.devcommunityapi.common.aws

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*


@Service
class AwsS3Service (
    private val amazonS3Client: AmazonS3Client,
) {

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    @Throws(IOException::class)
    fun uploadFiles(files: List<MultipartFile>?): List<String> {
        val imageUrls = ArrayList<String>()

        if (files != null) {
            for (file in files) {
                val originalFileName: String = UUID.randomUUID().toString() + "-" + file.originalFilename

                val objectMetadata = ObjectMetadata()
                objectMetadata.contentLength = file.size
                objectMetadata.contentType = file.contentType

                try {
                    amazonS3Client.putObject(bucket,originalFileName, file.inputStream, objectMetadata)
                    val uploadFileUrl = amazonS3Client.getUrl(bucket, originalFileName).toString()
                    imageUrls.add(uploadFileUrl)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return imageUrls
    }

    fun deleteFile(files: List<String>?) {
        if (files != null) {
            for (file in files) {
                val splitStr = ".com/"
                val fileName: String = file.substring(file.lastIndexOf(splitStr) + splitStr.length)
                val deleteObjectRequest = DeleteObjectRequest(bucket, fileName)
                amazonS3Client.deleteObject(deleteObjectRequest)
            }
        }
    }
}