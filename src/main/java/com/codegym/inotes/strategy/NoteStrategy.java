package com.codegym.inotes.strategy;
import com.codegym.inotes.model.Note; import java.io.IOException; import java.util.List;
public interface NoteStrategy { void save(Note note) throws IOException; void delete(int id) throws IOException; List<Note> findAll() throws IOException; List<Note> search(String keyword) throws IOException; }
