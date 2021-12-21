package com.base.baselibrary.utils.download

class DownloadException(val errorCode: Int) :
    Exception("Download Exception Error Code: $errorCode") {
}