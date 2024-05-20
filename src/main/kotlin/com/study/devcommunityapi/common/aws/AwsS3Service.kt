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
    fun uploadFiles(files: List<MultipartFile>): List<String> {
        val imageUrls = ArrayList<String>()

        for (file in files) {
//            if (fileUtil.isImageFile(originalFileName as String))
//                throw IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");
            val originalFileName: String = UUID.randomUUID().toString() + "-" + file.originalFilename

            val objectMetadata = ObjectMetadata()
            objectMetadata.contentLength = file.size
            objectMetadata.contentType = file.contentType

            try {
                amazonS3Client.putObject(bucket,originalFileName, file.inputStream, objectMetadata);
                val uploadFileUrl = amazonS3Client.getUrl(bucket, originalFileName).toString();
                imageUrls.add(uploadFileUrl)
            } catch (e: IOException) {
                e.printStackTrace();
            }
        }
        return imageUrls
    }

    //URL을 DB에 저장 후 DTO로 반환하기
//    @Transactional
//    fun saveFileUrls(userId:Int, imageUrls: List<String>, files: List<MultipartFile>): ArrayList<UploadLogDTO> {
//        val dtoList = ArrayList<UploadLogDTO>()
//
//        for ((index, file) in files.withIndex()) {
//
//            if(uploadUtils.isNotImageFile(file.originalFilename as String))
//                throw IllegalArgumentException("png, jpeg, jpg에 해당하는 파일만 업로드할 수 있습니다.");
//
//            val storeUrlDto = UploadLogDTO(
//                user_id = userId,
//                file_size = file.size.toInt(),
//                upload_date = LocalDateTime.now(),
//                url = imageUrls[index]
//            )
//            dtoList.add(storeUrlDto)
//            /* TODO Mapper 이용해 db 저장 하기 */
//        }
//
//        return dtoList
//    }

    fun deleteFile(s3FileName: String?) {
        val deleteObjectRequest = DeleteObjectRequest(bucket, s3FileName)
        amazonS3Client.deleteObject(deleteObjectRequest)
    }
}