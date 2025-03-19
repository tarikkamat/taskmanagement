package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService{
    private final AttachmentRepository attachmentRepository;
}
