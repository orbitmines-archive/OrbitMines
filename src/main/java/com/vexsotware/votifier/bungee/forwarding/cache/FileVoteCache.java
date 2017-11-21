package com.vexsotware.votifier.bungee.forwarding.cache;

import com.google.common.io.Files;
import com.vexsoftware.votifier.model.Vote;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class FileVoteCache extends MemoryVoteCache {

    private final File cacheFile;
    private final ScheduledTask saveTask;

    public FileVoteCache(int initialMemorySize, final Plugin plugin, File cacheFile) throws IOException {
        super(initialMemorySize);
        this.cacheFile = cacheFile;
        load();

        saveTask = ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            try {
                save();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Unable to save cached votes, votes will be lost if you restart.", e);
            }
        }, 3L, 3L, TimeUnit.MINUTES);
    }

    private void load() throws IOException {
        JSONObject object;

        try {
            BufferedReader reader = Files.newReader(cacheFile, StandardCharsets.UTF_8);
            Throwable localThrowable3 = null;
            try {
                object = new JSONObject(new JSONTokener(reader));
            } catch (Throwable localThrowable1) {
                localThrowable3 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (reader != null) {
                    if (localThrowable3 != null) {
                        try {
                            reader.close();
                        } catch (Throwable localThrowable2) {
                            localThrowable3.addSuppressed(localThrowable2);
                        }
                    } else {
                        reader.close();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            object = new JSONObject();
        }

        for (Object server : object.keySet()) {
            JSONArray voteArray = object.getJSONArray((String)server);
            Object votes = new ArrayList(voteArray.length());
            for (int i = 0; i < voteArray.length(); i++) {
                JSONObject voteObject = voteArray.getJSONObject(i);
                ((List)votes).add(new Vote(voteObject));
            }
            voteCache.put((String)server, (List) votes);
        }
    }

    public void save() throws IOException {
        cacheLock.lock();
        JSONObject votesObject = new JSONObject();
        Iterator localIterator1;
        Map.Entry<String, Collection<Vote>> entry = null;

        try {
            for (localIterator1 = voteCache.entrySet().iterator(); localIterator1.hasNext();) {
                entry = (Map.Entry) localIterator1.next();
                JSONArray array = new JSONArray();
                for (Vote vote : entry.getValue()) {
                    array.put(vote.serialize());
                }
                votesObject.put(entry.getKey(), array);
            }
        } finally {
            cacheLock.unlock();
        }

        BufferedWriter writer = Files.newWriter(cacheFile, StandardCharsets.UTF_8);
        Throwable throwable = null;

        try {
            votesObject.write(writer);
        } catch (Throwable localThrowable1) {
            throwable = localThrowable1;
            throw localThrowable1;
        } finally {
            if (writer != null) {
                if (entry != null) {
                    try {
                        writer.close();
                    } catch (Throwable localThrowable2) {
                        localThrowable2.addSuppressed(throwable);
                    }
                } else {
                    writer.close();
                }
            }
        }
    }

    public void halt() throws IOException {
        saveTask.cancel();
        save();
    }
}
