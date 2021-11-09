package project.ui;

import project.model.validare.ValidationException;
import project.service.ServiceException;
import project.service.Service;

import java.io.*;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

public class UI implements AutoCloseable{
    private final Service service;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final Map<String, UIFunction> commands;
    public UI(Service service, InputStream input, OutputStream output){
        this.service = service;
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new BufferedWriter(new OutputStreamWriter(output));
        commands = new TreeMap<>();
        commands.put("show",this::showAll);
        commands.put("add utilizator",this::addUtilizator);
        commands.put("update utilizator",this::updateUtilizator);
        commands.put("sterge utilizator",this::removeUtilizator);
        commands.put("add prieten",this::addPrieten);
        commands.put("sterge prieten",this::removePrieten);

        commands.put("numar comunitati",this::NumarComunitati);
        commands.put("sociabila",this::CeaMaiSociabila);

        commands.put("help",this::help);
    }

    /**
     * Porneste bucla de ascultare de comenzi
     */
    public void start()
    {
        try
        {
            while(true)
            {
                writer.write(">>>");
                writer.flush();
                String command = reader.readLine();
                if(command.equals("exit"))
                {
                    break;
                }
                var function = commands.get(command);
                if(function == null)
                {
                    writer.write("Comanda nu este recunoscuta!");
                    writer.newLine();
                    writer.write("Incearca comanda help!");
                    writer.newLine();
                    continue;
                }
                try
                {
                    function.execute();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                catch (NumberFormatException ex)
                {
                    writer.write("Valoarea introdusa nu este un numar intreg!");
                    writer.newLine();
                }
                catch (ServiceException ex)
                {
                    writer.write(ex.getMessage());
                    writer.newLine();
                }
                catch (ValidationException ex)
                {
                    writer.write(ex.getMessage());
                    writer.newLine();
                }
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Citeste un numar intreg din datele de intrare
     * @param message Mesajul care va fi afisat utilizatorului
     * @return Integer -> Numarul citit
     * @throws ParseException Daca formatul numarului nu este corect
     */
    private Integer readInteger(String message) throws IOException {
        writer.write(message);
        writer.flush();
        return Integer.parseInt(reader.readLine());
    }

    /**
     * Citeste un String din datele de intrare
     * @param message Mesajul care va fi afisat utilizatorului
     * @return String -> Stringul citit
     */
    private String readString(String message) throws IOException
    {
        writer.write(message);
        writer.flush();
        return reader.readLine();
    }

    /**
     * Afiseaza un meniu cu toate comenzile recunoscute
     */
    private void help() throws IOException
    {
        for(var comanda: commands.keySet())
        {
            writer.write(comanda);
            writer.newLine();
        }
        writer.write("exit");
        writer.newLine();
        writer.flush();
    }

    /**
     * Adauga un utilizator
     */
    private void addUtilizator() throws IOException
    {
        String name = readString("name=");
        service.addUtilizator(name);
        writer.write("Utilizator adaugat cu succes!\n");
        writer.newLine();
        writer.flush();
    }

    private void updateUtilizator() throws IOException
    {
        Integer id = readInteger("id=");
        String nume = readString("Noul nume=");
        service.updateUtilizator(id,nume);
    }

    /**
     * Sterge un utilizator
     */
    private void removeUtilizator() throws IOException
    {
        Integer id = readInteger("id=");
        service.removeUtilizator(id);
        writer.write("Utilizator sters cu succes!\n");
        writer.newLine();
        writer.flush();
    }

    /**
     * Adauga o relatie de prietenie
     */
    private void addPrieten() throws IOException
    {
        Integer idPrieten1 = readInteger("id utilizator=");
        Integer idPrieten2 = readInteger("id prieten=");
        service.addPrieten(idPrieten1,idPrieten2);
        writer.write("Prieten adaugat!");
        writer.newLine();
        writer.flush();
    }

    /**
     * Sterge o relatie de prietenie
     */
    private void removePrieten() throws IOException
    {
        Integer idPrieten1 = readInteger("id utilizator=");
        Integer idPrieten2 = readInteger("id prieten=");
        service.removePrieten(idPrieten1,idPrieten2);
        writer.write("Prieten sters!");
        writer.newLine();
    }

    /**
     * Afiseaza toti utilizatorii impreuna cu prietenii lor
     */
    private void showAll() throws IOException
    {
        writer.write("Utilizatorii sunt:");
        writer.newLine();
        for(var utilizator : service.findAll())
        {
            writer.write(utilizator.toStringWithFriends());
            writer.newLine();
        }
    }

    /**
     * Afiseaza numarul de comunitati
     */
    private void NumarComunitati() throws IOException
    {
        writer.write(String.format("Sunt %s comunitati", service.NumarComunitati()));
        writer.newLine();
        writer.flush();
    }

    /**
     * Afiseaza cea mai sociala comunitate
     */
    private void CeaMaiSociabila() throws IOException
    {
        writer.write("Cea mai sociabila comunitate este:");
        writer.newLine();
        writer.write(service.CeaMaiSociabila().toString());
        writer.newLine();
        writer.flush();
    }

    @Override
    public void close() {
        try
        {
            reader.close();
            writer.close();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}