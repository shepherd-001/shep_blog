package com.shepherd.shep_blog.services.reader;

import com.shepherd.shep_blog.data.dto.request.RegisterReaderRequest;
import com.shepherd.shep_blog.data.dto.response.RegisterReaderResponse;

public interface ReaderService {
    RegisterReaderResponse registerReader(RegisterReaderRequest request);
}
