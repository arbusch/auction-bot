import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    static class BidRequest {
        String category;
        int viewCount;
        int commentCount;
        boolean subscribed;
        String age;
        String gender;
        String[] interests;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int budget = Integer.parseInt(args[0]);
        int remaining = budget;

        // Choose category once
        System.out.println("ASMR");
        System.out.flush();

        String line;

        while (true) {
            line = reader.readLine();

            if (line == null) break;
            if (line.isEmpty()) continue;

            char first = line.charAt(0);

            if (first == 'v') { // video line

                BidRequest req = parse(line);

                int s = score(req);

                int startBid;
                int maxBid;

                // 🚨 Low-value impressions → minimal participation
                if (s <= 2) {
                    startBid = 1;
                    maxBid = Math.min(2, remaining);
                } else {
                    // 💰 scale bids based on value
                    maxBid = s * 3;

                    if (maxBid > remaining) maxBid = remaining;

                    startBid = maxBid / 2;
                    if (startBid < 1) startBid = 1;
                }

                System.out.println(startBid + " " + maxBid);
                System.out.flush();
            }

            else if (first == 'W') {
                int space = line.indexOf(' ');
                int cost = Integer.parseInt(line.substring(space + 1));
                remaining -= cost;
            }

            else if (first == 'L') {
                // do nothing
            }

            else if (first == 'S') {
                // could adapt strategy later
            }
        }
    }

    // 🧠 VALUE ESTIMATION FUNCTION
    static int score(BidRequest r) {
        int s = 0;

        // Strong category match
        if ("ASMR".equals(r.category)) s += 5;

        // Interest match
        if (r.interests != null) {
            for (int i = 0; i < r.interests.length; i++) {
                if ("ASMR".equals(r.interests[i])) {
                    s += 3;
                    break;
                }
            }
        }

        // Subscribed viewers are more valuable
        if (r.subscribed) s += 2;

        // Engagement ratio
        if (r.viewCount > 0) {
            double ratio = (double) r.commentCount / r.viewCount;

            if (ratio > 0.05) s += 3;
            else if (ratio > 0.02) s += 2;
            else if (ratio > 0.01) s += 1;
        }

        return s;
    }

    static BidRequest parse(String line) {
        BidRequest r = new BidRequest();

        String[] parts = line.split(",");

        for (int i = 0; i < parts.length; i++) {
            String p = parts[i];
            int eq = p.indexOf('=');

            String key = p.substring(0, eq);
            String val = p.substring(eq + 1);

            switch (key) {
                case "video.category":
                    r.category = val;
                    break;
                case "video.viewCount":
                    r.viewCount = Integer.parseInt(val);
                    break;
                case "video.commentCount":
                    r.commentCount = Integer.parseInt(val);
                    break;
                case "viewer.subscribed":
                    r.subscribed = val.equals("Y");
                    break;
                case "viewer.age":
                    r.age = val;
                    break;
                case "viewer.gender":
                    r.gender = val;
                    break;
                case "viewer.interests":
                    r.interests = val.split(";");
                    break;
            }
        }

        return r;
    }
}