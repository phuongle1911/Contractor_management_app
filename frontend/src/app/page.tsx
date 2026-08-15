"use client";

import { useEffect, useState } from "react";

type HealthResponse = {
  status: string;
};

const apiUrl = process.env.NEXT_PUBLIC_API_URL;

export default function Home() {
  const [apiStatus, setApiStatus] = useState(
    apiUrl ? "Checking..." : "Missing NEXT_PUBLIC_API_URL",
  );

  useEffect(() => {
    if (!apiUrl) {
      return;
    }

    const controller = new AbortController();

    fetch(`${apiUrl}/api/health`, {
      signal: controller.signal,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`API returned ${response.status}`);
        }

        return response.json() as Promise<HealthResponse>;
      })
      .then((data) => setApiStatus(data.status))
      .catch((error: unknown) => {
        if (error instanceof Error && error.name !== "AbortError") {
          setApiStatus("Unavailable");
        }
      });

    return () => controller.abort();
  }, []);

  return (
    <main className="flex min-h-screen items-center justify-center bg-zinc-50">
      <section className="rounded-xl border border-zinc-200 bg-white p-8">
        <h1 className="text-2xl font-semibold text-zinc-950">
          Contractor Management
        </h1>

        <p className="mt-4 text-zinc-600">
          Backend status:{" "}
          <strong className="text-zinc-950">{apiStatus}</strong>
        </p>
      </section>
    </main>
  );
}