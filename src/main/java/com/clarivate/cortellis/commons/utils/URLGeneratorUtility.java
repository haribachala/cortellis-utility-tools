package com.clarivate.cortellis.commons.utils;

import com.clarivate.cortellis.record.services.constants.RecordServiceConstants;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by U6015446 on 30-Nov-16.
 */
public class URLGeneratorUtility {


    public URI buildRequestPostURL(RecordServiceConstants recordServiceConstants){

        UriComponents requestPostURL = UriComponentsBuilder.newInstance()
                .scheme(recordServiceConstants.getProtocol())
                .host(recordServiceConstants.getHost())
                .port(recordServiceConstants.getPort())
                .path(recordServiceConstants.getUrlPath())
                .build();
        return requestPostURL.toUri();
    }

    public String generateSnapshotLoaderURL(RecordServiceConstants recordServiceConstants){

        UriComponents loaderURL = UriComponentsBuilder.newInstance()
                                       .scheme(recordServiceConstants.getProtocol())
                                       .port(recordServiceConstants.getPort())
                                       .host(recordServiceConstants.getHost())
                                       .path(recordServiceConstants.getRecordLoaderPath())
                                       .build();
        return loaderURL.toUriString();
    }
    public String generateSnapshotPublishURL(RecordServiceConstants recordServiceConstants){

        UriComponents loaderURL = UriComponentsBuilder.newInstance()
                .scheme(recordServiceConstants.getProtocol())
                .port(recordServiceConstants.getPort())
                .host(recordServiceConstants.getHost())
                .path(recordServiceConstants.getRecordPublishPath())
                .build();
        return loaderURL.toUriString();
    }
    public String generateSnapshotExtractURL(RecordServiceConstants recordServiceConstants){

        UriComponents loaderURL = UriComponentsBuilder.newInstance()
                .scheme(recordServiceConstants.getProtocol())
                .port(recordServiceConstants.getPort())
                .host(recordServiceConstants.getHost())
                .path(recordServiceConstants.getRecordExtractPath())
                .build();
        return loaderURL.toUriString();
    }
}
