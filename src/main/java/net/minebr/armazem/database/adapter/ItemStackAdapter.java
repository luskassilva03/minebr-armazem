package net.minebr.armazem.database.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minebr.armazem.database.util.Conversor;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class ItemStackAdapter extends TypeAdapter<ItemStack> {
    @Override//
    public void write(JsonWriter out, ItemStack item) throws IOException {
        out.beginObject();
        out.name("item").value(Conversor.serialize(item));
        out.endObject();
    }

    @Override
    public ItemStack read(JsonReader in) throws IOException {
        in.beginObject();

        ItemStack item = null;
        while (in.hasNext()) {
            if (in.nextName().equalsIgnoreCase("item")) {
                try {
                    item = Conversor.deserialize(in.nextString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        in.endObject();
        return item;
    }

}
