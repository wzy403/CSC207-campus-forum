package com.imperial.academia.data_access.remember_me;

import java.io.*;

public class RememberMeDAO implements RememberMeDAI {
    private static final String FILE_PATH = "database/rememberme.txt";

    @Override
    public void saveCredentials(String username, String password) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(username + "\n");
            writer.write(password + "\n");
        }
    }

    @Override
    public String[] loadCredentials() throws IOException {
        String[] credentials = new String[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            credentials[0] = reader.readLine();
            credentials[1] = reader.readLine();
        }
        return credentials;
    }

    @Override
    public void clearCredentials() throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write("");
        }
    }
}