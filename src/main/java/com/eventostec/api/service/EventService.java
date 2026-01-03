package com.eventostec.api.service;


import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private EventRepository eventRepository;

    public Event getEvent(UUID eventId) {
        return this.eventRepository.getReferenceById(eventId);
    }

    public List<EventResponseDTO> listEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = this.eventRepository.findAll(pageable);
        return eventPage.map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                "",
                "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl()
        )).toList();
    }

    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        if (data.image() != null) {
            imgUrl = this.uploadImg(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setDate(new Date(data.date()));
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());

        eventRepository.save(newEvent);

        return newEvent;
    };

    private String uploadImg(MultipartFile multipartFile) {
        String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            PutObjectRequest putOb = PutObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(putOb, RequestBody.fromByteBuffer(ByteBuffer.wrap(multipartFile.getBytes())));

            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build()).toExternalForm();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao subir arquivo para a Oracle Cloud", e);
        }
    };
}
