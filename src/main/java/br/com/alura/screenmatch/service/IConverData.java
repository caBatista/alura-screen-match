package br.com.alura.screenmatch.service;

public interface IConverData {
   <T> T getData(String json, Class<T> className);
}
