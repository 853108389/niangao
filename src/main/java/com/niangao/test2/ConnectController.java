package com.niangao.test2;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping({"/roomcenter/rooms/", "/ccccc"})
public class ConnectController {

    public ConnectController() {
    }

    @PostMapping({"{roomCode}/join", "aaaa"})
    public int connectRoom(@PathVariable("roomCode") String roomCode, @RequestHeader("sssId") String text, HttpServletRequest request, @RequestParam(name = "id") String roomUuid) {
        int i = 5;
        return i;
    }

    //
    @PostMapping
    public void connSfu(String bbb, @PathVariable("roomUuid") String aa, String ccc, HttpServletRequest cc) {
    }

//    public static void inputValueFromReq() {
//    }

    //    @PutMapping({"/{roomUuid}/endpoints/{endpointUid}"})
//    public void updateEndpointInfo(@PathVariable("roomUuid") String roomUuid, @PathVariable("endpointUid") String endpointUid) {
//    }
}
