package com.jobseeker.util;

import java.util.*;

public class ATSUtil {

    public static Map<String, Object> calculateATSScore(String resumeSkills, String resumeText, String jobSkills, String jobDescription) {

        Map<String, Object> result = new HashMap<>();

        if (jobSkills == null || jobSkills.isEmpty()) {
            result.put("score", 0);
            result.put("matchedSkills", Collections.emptyList());
            result.put("missingSkills", Collections.emptyList());
            return result;
        }

        // Convert to lowercase for matching
        resumeSkills = resumeSkills == null ? "" : resumeSkills.toLowerCase();
        resumeText = resumeText == null ? "" : resumeText.toLowerCase();
        jobSkills = jobSkills.toLowerCase();

        // Split skills list
        String[] jobSkillList = jobSkills.split(",");

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String skill : jobSkillList) {
            skill = skill.trim();
            if (skill.isEmpty()) continue;

            // Match skill with resume skill list OR resume text
            if (resumeSkills.contains(skill) || resumeText.contains(skill)) {
                matched.add(skill);
            } else {
                missing.add(skill);
            }
        }

        // Calculate score
        int total = jobSkillList.length;
        int matchedCount = matched.size();

        int score = (int) (((double) matchedCount / total) * 100);

        result.put("score", score);
        result.put("matchedSkills", matched);
        result.put("missingSkills", missing);

        return result;
    }
}

