package com.imperial.academia.service;

import com.imperial.academia.entity.chat_message.ChatMessage;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for Chat Message Service.
 */
public interface ChatMessageService {
    /**
     * Inserts a new chat message into the database.
     *
     * @param chatMessage the chat message to insert
     * @throws SQLException if a database access error occurs
     */
    void insert(ChatMessage chatMessage) throws SQLException;

    /**
     * Retrieves a chat message by its ID.
     *
     * @param id the ID of the chat message to retrieve
     * @return the chat message with the specified ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    ChatMessage get(int id) throws SQLException;

    /**
     * Retrieves all chat messages from the database.
     *
     * @return a list of all chat messages
     * @throws SQLException if a database access error occurs
     */
    List<ChatMessage> getAll() throws SQLException;

    /**
     * Updates an existing chat message in the database.
     *
     * @param chatMessage the chat message to update
     * @throws SQLException if a database access error occurs
     */
    void update(ChatMessage chatMessage) throws SQLException;

    /**
     * Deletes a chat message by its ID.
     *
     * @param id the ID of the chat message to delete
     * @throws SQLException if a database access error occurs
     */
    void delete(int id) throws SQLException;
}