package cn.shikh.utils.okhttp.request;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.builder.PostFormBuilder;
import cn.shikh.utils.okhttp.callback.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhy on 15/12/14.
 */
public class PostFormRequest extends OkHttpRequest
{
    private List<PostFormBuilder.FileInput> files;
    private List<PostFormBuilder.FileParams> fparams;
   /* public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, List<PostFormBuilder.FileInput> files)
    {
        super(url, tag, params, headers);
        this.files = files;
    }*/
    public PostFormRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers,List<PostFormBuilder.FileInput> files,List<PostFormBuilder.FileParams> fparams)
    {
        super(url, tag, params, headers);
        this.files = files;
        this.fparams = fparams;
    }
    @Override
    protected RequestBody buildRequestBody()
    {
        if (files == null || files.isEmpty())
        {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            return builder.build();
        } else
        {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder);

            for (int i = 0; i < files.size(); i++)
            {
                PostFormBuilder.FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            if(fparams != null && fparams.size()>0){
                for(int i = 0;i<fparams.size();i++){
                    PostFormBuilder.FileParams fileParams = fparams.get(i);
                    builder.addFormDataPart(fileParams.key,fileParams.value);
                }
            }

            return builder.build();
        }
    }

//    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback)
    {
        if (callback == null) return requestBody;
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener()
        {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength)
            {

                OkHttpUtils.getInstance().getDelivery().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.inProgress(bytesWritten * 1.0f / contentLength);
                    }
                });

            }
        });
        return countingRequestBody;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody)
    {
        return builder.post(requestBody).build();
    }

    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void addParams(MultipartBody.Builder builder)
    {
        if (params != null && !params.isEmpty())
        {
            for (String key : params.keySet())
            {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private void addParams(FormBody.Builder builder)
    {
        if (params != null)
        {
            for (String key : params.keySet())
            {
                builder.add(key, params.get(key));
            }
        }
    }

}
