package com.example.letteblack.util

import com.example.letteblack.R
import com.example.letteblack.model.CategoryModel
import com.example.letteblack.model.PuzzleModel
import com.example.letteblack.model.SubCourseModel

object MockData {
    fun mockCategories(): List<CategoryModel> = listOf(
        CategoryModel(
            categoryName = "Mathematics",
            subCourses = listOf(
                SubCourseModel("Algebra", R.drawable.ic_launcher_background),
                SubCourseModel("Geometry", R.drawable.ic_launcher_background),
                SubCourseModel("Trigonometry", R.drawable.ic_launcher_background),
                SubCourseModel("Calculus", R.drawable.ic_launcher_background),
            )
        ),
        CategoryModel(
            categoryName = "Science",
            subCourses = listOf(
                SubCourseModel("Physics Fundamentals", R.drawable.ic_launcher_background),
                SubCourseModel("Chemistry Basics", R.drawable.ic_launcher_background),
                SubCourseModel("Biology & Human Body", R.drawable.ic_launcher_background),
                SubCourseModel("Environmental Science", R.drawable.ic_launcher_background),
            )
        ),
        CategoryModel(
            categoryName = "History & Civics",
            subCourses = listOf(
                SubCourseModel("World History", R.drawable.ic_launcher_background),
                SubCourseModel("Indian Freedom Struggle", R.drawable.ic_launcher_background),
                SubCourseModel("Civics & Constitution", R.drawable.ic_launcher_background),
                SubCourseModel("Modern World Affairs", R.drawable.ic_launcher_background),
            )
        ),
        CategoryModel(
            categoryName = "Computer Science",
            subCourses = listOf(
                SubCourseModel("Programming Basics", R.drawable.ic_launcher_background),
                SubCourseModel("Data Structures", R.drawable.ic_launcher_background),
                SubCourseModel("Algorithms", R.drawable.ic_launcher_background),
                SubCourseModel("Artificial Intelligence", R.drawable.ic_launcher_background),
            )
        ),
        CategoryModel(
            categoryName = "Mobile Development",
            subCourses = listOf(
                SubCourseModel("Kotlin Basics", R.drawable.ic_launcher_background),
                SubCourseModel("Jetpack Compose", R.drawable.ic_launcher_background),
                SubCourseModel("Android Architecture Components", R.drawable.ic_launcher_background),
                SubCourseModel("Firebase Integration", R.drawable.ic_launcher_background),
            )
        ),
        CategoryModel(
            categoryName = "Web Development",
            subCourses = listOf(
                SubCourseModel("HTML & CSS", R.drawable.ic_launcher_background),
                SubCourseModel("JavaScript", R.drawable.ic_launcher_background),
                SubCourseModel("React.js", R.drawable.ic_launcher_background),
                SubCourseModel("Node.js", R.drawable.ic_launcher_background),
            )
        ),
        CategoryModel(
            categoryName = "Machine Learning",
            subCourses = listOf(
                SubCourseModel("Supervised Learning", R.drawable.ic_launcher_background),
                SubCourseModel("Unsupervised Learning", R.drawable.ic_launcher_background),
                SubCourseModel("Neural Networks", R.drawable.ic_launcher_background),
                SubCourseModel("NLP Basics", R.drawable.ic_launcher_background),
            )
        ),
        CategoryModel(
            categoryName = "Soft Skills",
            subCourses = listOf(
                SubCourseModel("Public Speaking", R.drawable.ic_launcher_background),
                SubCourseModel("Critical Thinking", R.drawable.ic_launcher_background),
                SubCourseModel("Teamwork & Collaboration", R.drawable.ic_launcher_background),
                SubCourseModel("Time Management", R.drawable.ic_launcher_background),
            )
        )
    )



    fun getPuzzleCategories(): List<PuzzleModel> = listOf(
        PuzzleModel("Logic Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Math Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Word Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Visual Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Coding Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Memory Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Pattern Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Riddle Challenges", R.drawable.ic_launcher_background),
        PuzzleModel("Brain Teasers", R.drawable.ic_launcher_background),
        PuzzleModel("Trivia Quizzes", R.drawable.ic_launcher_background),
        PuzzleModel("Jigsaw Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Sequence Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Crossword & Word Search", R.drawable.ic_launcher_background),
        PuzzleModel("Number Puzzles", R.drawable.ic_launcher_background),
        PuzzleModel("Picture Puzzles", R.drawable.ic_launcher_background)
    )


    // Function to get top puzzles
    fun getTopPuzzles(): List<PuzzleModel> = listOf(
        PuzzleModel("Sudoku", R.drawable.ic_launcher_background),
        PuzzleModel("Crossword", R.drawable.ic_launcher_background),
        PuzzleModel("Find the Difference", R.drawable.ic_launcher_background),
        PuzzleModel("Guess the Output (Code)", R.drawable.ic_launcher_background),
        PuzzleModel("Riddles", R.drawable.ic_launcher_background),
        PuzzleModel(
            title = "Visual Puzzle Game",
            image = R.drawable.ic_launcher_background,
            description = "guess the shape."
        )
    )
}