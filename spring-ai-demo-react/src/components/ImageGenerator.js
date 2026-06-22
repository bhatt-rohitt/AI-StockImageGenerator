import React, { useState } from "react";

function ImageGenerator() {
    const [prompt, setPrompt] = useState('');
    const [imageUrls, setImageUrls] = useState([]);
    const [loading, setLoading] = useState(false);

    const generateImage = async () => {
        if (!prompt.trim()) return;

        setLoading(true);

        try {
            const response = await fetch(
                `http://localhost:8080/generate-images?prompt=${encodeURIComponent(prompt)}&count=2`
            );

            // Handle backend errors properly
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || "Server error");
            }

            const data = await response.json();

            // Ensure we actually got an array
            if (!Array.isArray(data)) {
                throw new Error("Invalid response from server");
            }

            // Convert Base64 → usable image src
            const formattedImages = data.map(
                (img) => `data:image/png;base64,${img}`
            );

            setImageUrls(formattedImages);

        } catch (error) {
            console.error("Error generating image:", error);
            alert("Failed to generate image. Check backend.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="tab-content">
            <h2>Generate Image</h2>

            <input
                type="text"
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                placeholder="Enter prompt for image"
                style={{ padding: "8px", width: "300px" }}
            />

            <br /><br />

            <button onClick={generateImage} disabled={loading}>
                {loading ? "Generating..." : "Generate Image"}
            </button>

            <div
                className="image-grid"
                style={{ display: "flex", flexWrap: "wrap", marginTop: "20px" }}
            >
                {imageUrls.map((url, index) => (
                    <img
                        key={index}
                        src={url}
                        alt={`Generated ${index}`}
                        style={{
                            width: "200px",
                            height: "200px",
                            objectFit: "cover",
                            margin: "10px",
                            borderRadius: "10px"
                        }}
                    />
                ))}

                {/* Empty slots for UI consistency */}
                {[...Array(Math.max(0, 4 - imageUrls.length))].map((_, index) => (
                    <div
                        key={index}
                        style={{
                            width: "200px",
                            height: "200px",
                            border: "1px dashed #ccc",
                            margin: "10px",
                            borderRadius: "10px"
                        }}
                    ></div>
                ))}
            </div>
        </div>
    );
}

export default ImageGenerator;