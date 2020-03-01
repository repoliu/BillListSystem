package com.chinaoly.service;

import org.springframework.stereotype.Service;

@Service
public interface FileUploadServcice {

    int addFileListsByShoes(String file, Integer status) throws Exception;

    int addFileListsByTicket(String file, Integer status) throws Exception;
}
