package com.adoptpet.server.commons.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @titel SSE Emitter와 Event 데이터를 저장하는 캐시
 **/
@Repository
public class EmitterRepository {


    //Map에 회원과 연결된 SSE SseEmitter 객체를 저장
    public final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    //Event를 캐시에 저장
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();


    // id와 sseEmitter를 매개변수로 받아 emitters 맵에 저장
    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }

    //id와 event를 매개변수로 받아 eventCache 맵에 Event를 저장
    public void saveEventCache(String id, Object event) {
        eventCache.put(id, event);
    }

    //id로 시작하는 키를 가진 emitters 맵 항목을 필터링하여 맵으로 반환
    public Map<String, SseEmitter> findAllStartWithById(String id) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //id로 시작하는 키를 가진 eventCache 맵 항목을 필터링하여 맵으로 반환
    public Map<String, Object> findAllEventCacheStartWithId(String id) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // id로 시작하는 키를 가진 emitters 맵 항목을 모두 제거
    public void deleteAllStartWithId(String id) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(id)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    //  emitters 맵에서 해당 id를 가진 항목을 삭제
    public void deleteById(String id) {
        emitters.remove(id);
    }


    // id로 시작하는 키를 가진 eventCache 맵 항목을 모두 제거
    public void deleteAllEventCacheStartWithId(String id) {
        eventCache.forEach(
                (key, data) -> {
                    if (key.startsWith(id)) {
                        eventCache.remove(key);
                    }
                }
        );
    }


}

