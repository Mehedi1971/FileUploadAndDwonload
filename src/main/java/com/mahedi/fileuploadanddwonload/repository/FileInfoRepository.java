package com.mahedi.fileuploadanddwonload.repository;

import com.mahedi.fileuploadanddwonload.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo,Long> {

}
