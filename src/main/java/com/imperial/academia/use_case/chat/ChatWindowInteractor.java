package com.imperial.academia.use_case.chat;

import com.imperial.academia.service.AudioService;
import com.imperial.academia.service.ChatMessageService;
import com.imperial.academia.session.SessionManager;
import com.imperial.academia.entity.chat_message.ChatMessage;
import com.imperial.academia.entity.chat_message.ChatMessageDTO;
import com.imperial.academia.entity.chat_message.ChatMessageFactory;

import java.util.List;

public class ChatWindowInteractor implements ChatWindowInputBoundary {
    private final ChatMessageService chatMessageService;
    private final AudioService audioService;
    private final ChatWindowOutputBoundary chatWindowPresenter;
    private final ChatMessageFactory chatMessageFactory;

    public ChatWindowInteractor(ChatMessageService chatMessageService, AudioService audioService, ChatWindowOutputBoundary chatWindowPresenter, ChatMessageFactory chatMessageFactory) {
        this.chatMessageService = chatMessageService;
        this.audioService = audioService;
        this.chatWindowPresenter = chatWindowPresenter;
        this.chatMessageFactory = chatMessageFactory;
    }

    @Override
    public void execute(int chatGroupId) {
        try {
            List<ChatMessageDTO> chatMessages = chatMessageService.getAllByGroupId(chatGroupId);
            ChatWindowOutputData chatWindowOutputData = new ChatWindowOutputData(chatMessages, chatGroupId);
            chatWindowPresenter.presentChatMessages(chatWindowOutputData);
        } catch (Exception e) {
            chatWindowPresenter.presentError("An error occurred while loading chat messages.");
        }
    }

    @Override
    public void startRecording(int chatGroupId) {
        try {
            audioService.startRecording(chatGroupId);
        } catch (Exception e) {
            chatWindowPresenter.presentError("An error occurred while starting recording.");
        }
    }

    @Override
    public void stopRecording() {
        try {
            audioService.stopRecording();
        } catch (Exception e) {
            chatWindowPresenter.presentError("An error occurred while stopping recording.");
        }
    }

    @Override
    public void loadAudio(String audioPath) {
        try {
            audioService.loadAudio(audioPath);
        } catch (Exception e) {
            chatWindowPresenter.presentError("An error occurred while loading audio.");
        }
    }

    @Override
    public void sendMessage(ChatWindowInputData chatWindowInputData) {
        int senderId = SessionManager.getCurrentUser().getId();
        int groupId = chatWindowInputData.getChatGroupId();
        String contentType = chatWindowInputData.getContentType();
        String content = chatWindowInputData.getContent();
        ChatMessage chatMessage = chatMessageFactory.createChatMessage(senderId, -1, groupId, contentType, content);
        try {
            chatMessageService.insert(chatMessage);
        } catch (Exception e) {
            chatWindowPresenter.presentError("An error occurred while sending message.");
        }
        execute(groupId);
    }
}
