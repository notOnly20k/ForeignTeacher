package com.xld.foreignteacher.Service

import android.content.Context
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider
import com.alibaba.sdk.android.oss.model.ObjectMetadata
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.xld.foreignteacher.ext.e
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/12/18.
 */
class OssHandler(context: Context) {
    private val logger = LoggerFactory.getLogger("OssHandler")
    private val endpoint = "http://oss-cn-beijing.aliyuncs.com"
    private val stsServer = "http://foreignteachers.oss-cn-beijing.aliyuncs.com"
    private val bucket: String = "foreignteachers"
    private val Access_Key_ID = "LTAIw6ll8NkAFjbg"
    private val Access_Key_Secret = "S9mz3JRowDqLbYJr6EYlgLSZ8ysdAM"
    private val oss: OSSClient

    init {

        //推荐使用OSSAuthCredentialsProvider。token过期可以及时更新
        val credentialProvider = OSSPlainTextAKSKCredentialProvider(Access_Key_ID,Access_Key_Secret)
        //该配置类如果不设置，会有默认配置，具体可看该类
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
        conf.maxConcurrentRequest = 5 // 最大并发请求数，默认5个
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次

        oss = OSSClient(context, endpoint, credentialProvider)
    }

    fun ossUpload(strPath: String) {
        logger.e { strPath }
        // 指定数据类型，没有指定会自动根据后缀名判断
        val objectMeta = ObjectMetadata()
        objectMeta.contentType = "image/jpeg"

        // 构造上传请求
        // 这里的objectKey其实就是服务器上的路径，即目录+文件名
        //因为目录命名逻辑涉及公司信息，被我删去，造成不知道这个objectKey不知为何物，如下是我们公司的大致命名逻辑
        //String objectKey = keyPath + "/" + carArr[times] + ".jpg";
        val put = PutObjectRequest(bucket, strPath, strPath)
        put.metadata = objectMeta
        try {
            oss.putObject(put)
        } catch (e: ClientException) {
            e.printStackTrace()
        } catch (e: ServiceException) {
            e.printStackTrace()
        }

        // 异步上传时可以设置进度回调
        put.progressCallback = OSSProgressCallback { request, currentSize, totalSize ->
            logger.error("PutObject", "currentSize: $currentSize totalSize: $totalSize")
        }
        val task = oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest, result: PutObjectResult) {
                logger.e{"UploadSuccess"}
                logger.error("ETag", result.eTag)
                logger.error("RequestId", result.requestId)
            }

            override fun onFailure(request: PutObjectRequest, clientExcepion: ClientException?, serviceException: ServiceException?) {
                // 请求异常
                clientExcepion?.printStackTrace()
                if (serviceException != null) {
                    // 服务异常
                    logger.e { "ErrorCode ${serviceException.errorCode}" }
                    logger.e { "RequestId ${serviceException.requestId}" }
                    logger.e { "requestId ${serviceException.hostId}" }
                    logger.e { "rawMessage ${serviceException.rawMessage}" }
                }
            }
        })

    }
}
