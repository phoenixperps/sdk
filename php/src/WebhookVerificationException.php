<?php

declare(strict_types=1);

namespace PhoenixPerps;

use PhoenixPerps\Exception\PhoenixPerpsException;

/**
 * Why a delivery did not verify (a PhoenixPerpsException): missing_headers, stale, bad_signature, or bad_body (signed,
 * but not an event).
 */
final class WebhookVerificationException extends PhoenixPerpsException
{
    public function __construct(string $errorCode, string $message)
    {
        parent::__construct($errorCode, $message);
    }
}
