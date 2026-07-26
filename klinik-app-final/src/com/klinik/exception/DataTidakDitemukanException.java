package com.klinik.exception;

// EXCEPTION HANDLING: custom checked exception
public class DataTidakDitemukanException extends Exception {
    public DataTidakDitemukanException(String pesan) {
        super(pesan);
    }
}
